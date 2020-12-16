import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object Task {
  def longRunningTask(from: Int, until: Int): Seq[Int] = {
    println(s"generator inputs: from $from until $until")
    Thread.sleep(2000)
    val res = (from to until).toVector.map(_ * 2)

    println(s"generated nums from ${res.head} to ${res.last}")
    res
  }

  def run = {
    val f1 = Future(longRunningTask(0, 100))
    val f2 = Future(longRunningTask(100, 200))

    val result = for {
      a <- f1
      b <- f2
      c <- Future(longRunningTask(a.head, b.last))
    } yield c

    println(Await.result(result, 10.seconds))
  }
}

Task.run
