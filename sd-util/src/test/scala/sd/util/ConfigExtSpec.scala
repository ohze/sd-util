package sd.util

import com.typesafe.config.ConfigFactory
import org.specs2.concurrent.ExecutionEnv
import org.specs2.matcher.Matchers
import org.specs2.mutable.Specification
import sd.util.ConfigExt.Implicits

class ConfigExtSpec(implicit ee: ExecutionEnv) extends Specification with Matchers {
  "ConfigExt normal case" >> {
    val c = ConfigFactory.parseString(
      """
        |foo {
        |    baz: [a,b,cd]
        |    cards {
        |        vms:    []
        |        vina:   [0,0,100]
        |    }
        |}
      """.stripMargin
    )
    c.getStringListEx("foo.baz") must_=== Seq("a", "b", "cd")
    c.getIntListEx("foo.cards.vms") must_=== Seq.empty[Int]
    c.getIntListEx("foo.cards.vina") must_=== Seq(0,0,100)
  }

  "ConfigExt can use Env Variables (string)" >> {
    val c0 = ConfigFactory.parseString(
      """
        |foo {
        |    baz=[a,b,cd]
        |    baz= ${?BOO_BAZ}
        |    cards {
        |        vina:   [0,0,100]
        |        vina = ${?CARDS_VINA}
        |    }
        |}
      """.stripMargin
    )
    EnvHack.setEnv(Map("CARDS_VINA" -> "[1, 4,3]"))
    val c = c0.resolve()
    c.getStringListEx("foo.baz") must_=== Seq("a", "b", "cd")
    c.getIntListEx("foo.cards.vina") must_=== Seq(1, 4, 3)
  }
}
