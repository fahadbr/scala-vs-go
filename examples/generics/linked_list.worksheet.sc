


sealed trait List[+A] {
  val headOption: Option[A]
  def ::[B >: A](e: B): List[B] = Cons(e, this)
}

case object Nil extends List[Nothing] {
  val headOption: Option[Nothing] = None
}

case class Cons[+A](head: A, tail: List[A]) extends List[A] {
  val headOption: Option[A] = Some(head)
}

def stringExample() =
  getHeadUppercase("a" :: "b" :: "c" :: Nil)

def intExample() =
  getHeadDoubled(1 :: 2 :: 3 :: Nil)

def getHeadUppercase(list: List[String]) =
  list.headOption.map { head =>
    s"head is ${head.toUpperCase} when upper cased"
  }

def getHeadDoubled(list: List[Int]) =
  list.headOption.map { head =>
    s"head is ${head * 2} when doubled"
  }

stringExample()
intExample()


// Covariance{{{ //

val i: Int = 42
val a: Any = i
val intList: List[Int] = 1 :: 3 :: i :: Nil
val anyList: List[Any] = intList

val anyListWithString = "hi there" :: anyList
val anyListWithString2 = "hi there" :: intList

// }}} Covariance //
