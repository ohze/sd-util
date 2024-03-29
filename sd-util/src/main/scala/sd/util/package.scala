package sd

package object util {
  @deprecated("Use [[scala.collection.mutable.rnd.HashSet]]", "1.2.0")
  type RandomIterHashSet[A] = scala.collection.mutable.rnd.HashSet[A]

  implicit class DoSomeOps[A](private val it: Iterable[A]) extends AnyVal {

    /** not-so-optimized version of `it.take(count).foreach(f)` but ensure that a new Collection is not built
      * @see [[scala.collection.mutable.rnd.HashSet]]
      */
    @deprecated("use `.iterator.take(count).foreach(f)`", "1.2.0")
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
