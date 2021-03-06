package dolevYao

import scala.collection.mutable

class CollapsingRelation(val n: Int) {
  // Maps the endpoints of a collapsing path to the path itself.
  val paths: mutable.Map[(Int, Int), List[Operator]] = mutable.Map.empty
  // Represents the membership.
  val membership: Array[Array[Boolean]] = Array.ofDim(n, n)
  // Each possible self-loop is already a collapsing path. Add them.
  Range(0, n).foreach(i => {
    membership(i)(i) = true
    paths((i, i)) = List.empty
  })

  // Returns whether (i, j) in C
  def in(i: Int, j: Int): Boolean = membership(i)(j)
  // Returns the collapsing path from i to j iff (i, j) in C.
  def getPath(i: Int, j: Int): Option[List[Operator]] = paths.get((i, j))

  // We are given (i, j) in C and (j, k) in C already. Combines them.
  def extendRight(i: Int, j: Int, k: Int): Unit = {
    membership(i)(k) = true
    paths((i, k)) = paths((i, j)).appendedAll(paths((j, k)))
  }

  // We are given (i, j) in C and (k, i) in C already. Combines them.
  def extendLeft(i: Int, j: Int, k: Int): Unit = {
    membership(k)(j) = true
    paths((k, j)) = paths((k, i)).appendedAll(paths((i, j)))
  }

  // Extend (i, j) s.t. k =sigma=> (i, j) =theta=> l
  def extendBoth(i: Int, j: Int, k: Int, l: Int, sigma: Operator, theta: Operator): Unit = {
    membership(k)(l) = true
    paths((k, l)) = sigma :: (paths((i, j)) :+ theta)
  }
}