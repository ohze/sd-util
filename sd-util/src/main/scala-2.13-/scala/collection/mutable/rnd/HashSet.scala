/** @author giabao
  * created: 2013-02-01 20:59
  * Copyright(c) 2011-2012 sandinh.com
  */
package scala.collection.mutable.rnd

import scala.collection.{AbstractIterator, mutable}
import scala.util.Random

/** Random Iterable HashSet */
final class HashSet[A] extends mutable.HashSet[A] { // extends HashSet to access field `table`
  import collection.Iterator
  override def iterator: Iterator[A] = new AbstractIterator[A] {
    private[this] val pivot = Random.nextInt(table.length) //table.length is always > 0
    private[this] val len1 = table.length - 1
    private[this] var i = pivot - 1; if (i < 0) i = len1
    def hasNext: Boolean = {
      while (i != pivot && (null eq table(i))) {
        i -= 1; if (i < 0) i = len1
      }
      i != pivot
    }
    def next(): A =
      if (hasNext) {
        val ret = table(i)
        i -= 1; if (i < 0) i = len1
        entryToElem(ret)
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
