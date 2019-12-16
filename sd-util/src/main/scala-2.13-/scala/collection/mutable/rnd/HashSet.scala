/** @author giabao
  * created: 2013-02-01 20:59
  * Copyright(c) 2011-2012 sandinh.com
  */
package scala.collection.mutable.rnd

import scala.collection.mutable
import scala.util.Random

/** Random Iterable HashSet */
final class HashSet[A] extends mutable.HashSet[A] { // extends HashSet to access field `table`
  import collection.Iterator
  override def iterator: Iterator[A] = new Iterator[A] {
    private val pivot = Random.nextInt(table.length) //table.length is always > 0
    private var i = pivot - 1
    @inline private def norm(): Unit = if (i < 0) i = table.length - 1
    norm()
    @inline private def nextI(): Unit = { i -= 1; norm() }
    def hasNext: Boolean = {
      while (i != pivot && (null == table(i))) nextI()
      i != pivot
    }
    def next(): A =
      if (hasNext) {
        val ret = table(i)
        nextI()
        ret.asInstanceOf[A]
      } else {
        Iterator.empty.next()
      }
  }

  def doSome[U](numUserToDo: Int, f: A => U): Unit = {
    var n = numUserToDo
    val i = iterator
    while (n > 0 && i.hasNext) {
      n -= 1
      f(i.next())
    }
  }
}