package sd

package object util {
  /** @deprecated since 1.2.0 - use [[scala.collection.mutable.rnd.HashSet]] */
  type RandomIterHashSet[A] = scala.collection.mutable.rnd.HashSet[A]

  implicit class DoSomeOps[A](val it: Iterable[A]) extends AnyVal {
    /** not-so-optimized version of `it.take(count).foreach(f)` but ensure that a new Collection is not built
     * @deprecated use `.iterator.take(count).foreach(f)`
     * @see [[scala.collection.mutable.rnd.HashSet]]
     */
    def doSome[U](count: Int, f: A => U): Unit = {
      var n = count
      val i = it.iterator
      while (n > 0 && i.hasNext) {
        n -= 1
        f(i.next())
      }
    }
  }
}
