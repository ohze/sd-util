/** @author giabao
  * created: 2013-02-01 20:59
  * Copyright(c) 2011-2012 sandinh.com
  */
package scala.collection.mutable.rnd

import scala.collection.{AbstractIterator, mutable}
import scala.util.Random

/** Random Iterable HashSet
 * Different from scala.collection.mutable.HashSet, this is a RandomIterableHashSet
 * {{{
 *   import scala.collection.mutable.rnd
 *   val waitingUsers: rnd.HashSet[User] = ???
 *   val numUserToDo: Int = ???
 *   waitingUsers
 *     .iterator // must use `iterator` for random feature
 *     .take(numUserToDo) // may return less than numUserToDo if waitingUsers.length < numUserToDo
 *     .foreach { user => /* do whatever with this user */ }
 * }}}
 * Notes:
 * + rnd.HashSet is exactly == collection.mutable.HashSet except that it randomly iterating
 * + If you iterate directly, ex `aHashSet.foreach` then the iterating order will not random
 *   (must use `aHashSet.iterator.blabla`)
 * + Each time you call `.iterator` then the `.next()` will be random. But given:
 *   {{{
 *     val h = rnd.HashSet(1,2,3,4)
 *     val i1 = h.iterator
 *     val i2 = h.iterator
 *   }}}
 *   Then:
 *   - i1.next() can ! i2.next()
 *   - but if i1.next() `n` times == i2.next() // 2 times mean i1.next().next()
 *         then i1.next() `n + 1` times will always == i2.next().next()
 */
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
}
