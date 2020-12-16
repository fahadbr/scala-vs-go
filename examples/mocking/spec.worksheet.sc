import $ivy.`org.scalatest::scalatest:3.1.1`
import $ivy.`org.scalamock::scalamock:4.4.0`
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite
import org.scalamock.scalatest.MockFactory
import org.scalatest.run

trait Formatter {
  def format(s: String): String
}

def sayHello(name: String, formatter: Formatter) = {
  formatter.format(name)
}

class FormatterSpec
    extends AnyFunSpec
    with MockFactory
    with Matchers {

  describe("sayHello") {
    val mockFormatter = mock[Formatter]
    val answer = "Ah, Mr Bond. I've been expecting you"

    (mockFormatter.format _)
      .expects("Mr Bond")
      .returning(answer)

    it("should respond with the appropriate response") {
      val actual = sayHello("Mr Bond", mockFormatter)
      actual should be(answer)
    }
  }
}

org.scalatest.run(new FormatterSpec)
