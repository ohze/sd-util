package scala.collection.mutable.rnd

import org.specs2.matcher
import org.specs2.mutable.Specification
import sd.util.DoSomeOps

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class HashSetSpec extends Specification {
  "(RandomIter)HashSet" >> {
    val seq = Seq(3, 3, 0, 2, 4, -3, 1 - 3, 0, 9, -1)
    val h = new HashSet[Int] ++= seq
    val n = 5 //number of elems to takeSome
    def takeSome(): Seq[Int] = {
      val l = ListBuffer.empty[Int]
      h.doSome(n, l.+=)
      l.toSeq
    }
    //noinspection ScalaStyle
    def randomly(f: () => Seq[Int]): Boolean = {
      for (i <- 0 to 10) {
        val l1 = f()
        val l2 = f()
        if (l1.size != l2.size || l1.size != n)
          return false
        if ((l1 zip l2).exists(z => z._1 != z._2)) {
          println(
            s"random at round $i. First elems are ${l1.head} != ${l2.head}"
          )
          return true
        }
      }
      false
    }

    "have right elems" >> {
      seq.distinct.length === 8
      h.size === 8
      h must containTheSameElementsAs(seq.distinct)
    }

    "random iterable" >> {
      takeSome() must haveSize(n)

      randomly(takeSome) === true
    }

    "random iterable using `.iterator.take`" >> {
      randomly(() => h.iterator.take(n).toSeq) === true
    }

    "random iterable using `.take`" >> {
      randomly(() => h.take(n).toSeq) === true
    }
    "random iterable using `.view.take`" >> {
      randomly(() => h.view.take(n).toSeq) === true
    }
  }
}
