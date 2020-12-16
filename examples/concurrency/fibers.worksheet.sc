import $ivy.`dev.zio::zio:1.0.3`
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.Executors
import scala.concurrent.ExecutionContextExecutorService
import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.ExecutionContext
import scala.Console.{GREEN, BLUE}
import zio._
import zio.console.putStrLn
import zio.duration._
import zio.internal.Executor

object Concurrency {

  val maxVals = 5
  val schedule = Schedule.spaced(500.millis) && Schedule.recurs(maxVals)
  val getFiberId = ZIO.fiberId.map { id =>
    val threadName = Thread.currentThread().getName()
    s"[${threadName}__fiber-${id.seqNumber}]"
  }

  def produce(q: Queue[Int], counter: Ref[Int]) = {
    for {
      i <- counter.getAndUpdate(_ + 1)
      id <- getFiberId
      _ <- putStrLn(s"${BLUE}$id pushing $i to queue")
      _ <- q.offer(i)
      _ <- putStrLn(s"${BLUE}$id pushed $i to queue")
    } yield ()
  }

  def consume(q: Queue[Int]) = {
    for {
      id <- getFiberId
      _ <- putStrLn(s"${GREEN}$id popping from queue")
      i <- q.take
      _ <- putStrLn(s"${GREEN}$id popped $i from queue")
    } yield i
  }

  def runAsync(counter: Ref[Int]) = {
    for {
      queue <- Queue.bounded[Int](maxVals)
      fiber1 <- produce(queue, counter).repeat(schedule).fork
      fiber2 <- consume(queue).repeatN(maxVals).fork
      _ <- fiber1.join
      queueResults <- fiber2.join
    } yield queueResults
  }

  def runWithSingleThread = {
    Runtime.default
      .withExecutor(
        Executor.fromThreadPoolExecutor(_ => 2)(
          Executors.newFixedThreadPool(1).asInstanceOf[ThreadPoolExecutor]
        )
      )
      .unsafeRun(Ref.make(0).flatMap(Concurrency.runAsync(_)))
  }

}

Concurrency.runWithSingleThread
