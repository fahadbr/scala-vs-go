// vim:foldmethod=marker
//
import scala.util.Failure
import scala.util.Success
import scala.util.Failure
import scala.util.Try

// Error definition {{{ //
type Error = Throwable
type Err = String

object Error {
  def apply(msg: String): Error = new Exception(msg)
}

object Err {
  def apply(msg: String) = msg
}
// }}} Error definition //

// GoLikeErrors {{{ //
object GoLikeErrors {
  def parseInt(numString: String): (Int, Err) =
    Try(numString.toInt) match {
      case Failure(e)     => (0, e.getMessage)
      case Success(value) => (value, null)
    }

  def getSqrt(i: Int): (Int, Err) = {
    val res = Math.sqrt(i)

    if (!res.isWhole) {
      return (0, Err(s"square root of $i is not a whole number"))
    }

    return (res.toInt, null)
  }

  def doWork(input: String): (Int, Err) = {
    val (i, err) = parseInt(input)

    if (err != null) {
      return (0, err)
    }

    val (sqrt, err2) = getSqrt(i)
    if (err2 != null) {
      return (0, err2)
    }

    return (sqrt, null)
  }
}

val doWork = GoLikeErrors.doWork _
doWork("100")
doWork("100a")
doWork("101")
// }}} GoLikeErrors //

// ExceptionErrors {{{ //

object ExceptionErrors {

  def parseInt(numString: String): Int =
    numString.toInt

  def getSqrt(i: Int): Int = {
    val res = Math.sqrt(i)

    if (res.isWhole)
      res.toInt
    else
      throw new Exception(s"square root of $i is not a whole number")
  }

  def doWork(input: String): Int = {
    getSqrt(parseInt(input))
  }
}

val doWork2 = ExceptionErrors.doWork _
Try(doWork2("100"))
Try(doWork2("100a"))
Try(doWork2("101"))

// }}} ExceptionErrors //

// TryErrors {{{ //
object TryErrors {

  def parseInt(numString: String): Try[Int] = Try {
    numString.toInt
  }

  def getSqrt(i: Int): Try[Int] = Try {
    val res = Math.sqrt(i)

    if (res.isWhole)
      res.toInt
    else
      throw new Exception(s"square root of $i is not a whole number")
  }

  def doWork(input: String): Try[Int] = {
    for {
      i <- parseInt(input)
      sqrt <- getSqrt(i)
    } yield sqrt
  }
}

val doWork3 = TryErrors.doWork _
doWork3("100")
doWork3("100a")
doWork3("101")
// }}} TryErrors //

// EitherErrors {{{ //

object EitherErrors {

  def parseInt(numString: String): Either[Error, Int] = Try {
    numString.toInt
  }.toEither

  def getSqrt(i: Int): Either[Error, Int] = {
    val res = Math.sqrt(i)

    if (res.isWhole)
      Right(res.toInt)
    else
      Left(new Exception(s"square root of $i is not a whole number"))
  }

  def doWork(input: String): Either[Error, Int] = {
    for {
      i <- parseInt(input)
      sqrt <- getSqrt(i)
    } yield sqrt
  }
}

val doWork4 = EitherErrors.doWork _
doWork4("100")
doWork4("100a")
doWork4("101")

// }}} EitherErrors //
