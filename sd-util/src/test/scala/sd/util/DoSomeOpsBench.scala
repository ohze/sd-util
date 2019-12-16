package sd.util

import scala.collection.mutable.rnd.HashSet
import scopt.OParser

object Algs extends Enumeration {
  type Algs = Value
  val DoSome, Take, ViewTake = Value
}

import Algs._
private case class Config(num: Int = 10000, rTake: Double = .9, alg: Algs = DoSome) {
  def take = (num * rTake).toInt
}
object DoSomeOpsBench {
  implicit val algRead: scopt.Read[Algs.Value] =
    scopt.Read.reads(Algs.withName)

  /** Pls run by: sbt "+sd-util / test:run" */
  def main(args: Array[String]): Unit = {
    val builder = OParser.builder[Config]
    val parser1 = {
      import builder._
      OParser.sequence(
        opt[Int]('n', "num")
          .action((x, c) => c.copy(num = x))
          .text("num of elem in will-create HashSet"),
        opt[Double]('f', "r-take")
          .action((x, c) => c.copy(rTake = x))
          .text("ratio of number of processing item / `num`. In [0, 1]")
          .validate {
            case f if f <= 0 => Left("must >0")
            case f if f > 1 => Left("must <= 1")
            case _ => Right(())
          },
        opt[Algs]('a', "alg")
          .action((x, c) => c.copy(alg = x))
      )
    }
    OParser.parse(parser1, args, Config()).foreach(run)
  }

  def foo(i: Int): Unit = {}

  implicit class FormatNumber(val n: Long) extends AnyVal {
    def fmt: String = {
      val s = n.toString
      val prefix = " " * (s.length % 3)
      s"$prefix$s".grouped(3).mkString(".").dropWhile(_ == ' ')
    }
  }

  def run(c: Config): Unit = {
    val h = new HashSet[Int]()
    h ++= Range(0, c.num)

    def bench(alg: Algs): Long = {
      val t1 = System.nanoTime()
      alg match {
        case DoSome => h.doSome(c.take, foo)
        case Take => h.take(c.take).foreach(foo)
        case ViewTake => h.view.take(c.take).foreach(foo)
      }
      val t2 = System.nanoTime()
      t2 - t1
    }
    val TDoSome = bench(DoSome)
    val TTake = bench(Take)
    val TViewTake = bench(ViewTake)
    val best = TDoSome min TTake min TViewTake match {
      case TDoSome => DoSome
      case TTake => Take
      case TViewTake => ViewTake
      case _ => ???
    }

    println(
      s"Bench:\nDoSome\t\t\tTake\t\t\tViewTake\n" +
      s"${TDoSome.fmt}\t\t${TTake.fmt}\t\t${TViewTake.fmt}\n" +
      s"Best: $best")
  }
}
