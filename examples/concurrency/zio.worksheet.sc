import $ivy.`dev.zio::zio:1.0.3`
import zio._
import zio.console._
import zio.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object Concurrency {

  def runZio(i: Int) = {
    for {
      _ <- putStrLn(s"started task $i")
      _ <- ZIO.sleep(3.seconds)
      _ <- putStrLn(s"finished task $i")
    } yield i
  }

  def runAllZios = {
    ZIO.collectAllPar(
      (0 to 5).map(runZio)
    )
  }
}

Runtime.default.unsafeRun(Concurrency.runAllZios)
