package sd.util

import org.specs2._
import scala.util.{Failure, Success, Try}

class HmacSHA1Spec extends Specification with ScalaCheck {
  def is =
    s2"sd.util.HmacSHA1 in `sd-util:1.0.3` is same as in our new version ${prop(check)}"

  private val check = (message: String, secret: String) => {
    val v201 = Try { HmacSHA1.signHmacSHA1hex(message, secret) }
    val v103 = Try { HmacSHA1v103.signHmacSHA1hex(message, secret) }
    (v201, v103) match {
      case (Success(a), Success(b)) => a == b
      case (Failure(a), Failure(b)) =>
        a.getClass == b.getClass && a.getMessage == b.getMessage
      case _ => false
    }
  }
}
