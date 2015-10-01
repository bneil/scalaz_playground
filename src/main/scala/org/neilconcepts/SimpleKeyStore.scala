package org.neilconcepts

import scalaz._; import Scalaz._
import org.scalameter._
import scalaz.Memo._

object SimpleKeyStore extends App {
  private val mutCache = scala.collection.mutable.Map[Int, String]()
  private def setCache(a: Int): String = s"entry:$a"
  private val cache = immutableHashMapMemo {
    entry: Int => setCache(entry)
  }

  private def init {
    val time = measure {
      for (i <- 1 |-> 1000000) {
        val r = scala.util.Random.nextInt(1000)
        cache(r)
      }
    }
    println(s"Total time: $time")

    val time2 = measure {
      for (i <- 1 |-> 1000000) {
        val r = scala.util.Random.nextInt(1000)
        mutCache(i) = s"entry:$i"
      }
    }
    println(s"Total time: $time2")
  }

  init
}
