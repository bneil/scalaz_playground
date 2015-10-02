package org.neilconcepts

import scalaz._; import Scalaz._
import org.scalameter._

object Ternany extends App {

  private def init {
    val time = measure {
      for (i <- 1 |-> 100000) {
        val a = i % 2 == 0
        val x = a ? 1 | 0
      }
    }
    println(s"scalaz) Total time: $time")

    var a = null
    val time2 = measure {
      for (i <- 1 |-> 100000) yield {
        val a = i % 2 == 0
        val b = if(a) true else false
      }
    }

    println(s"scala) Total time: $time2")
  }

  init
}

