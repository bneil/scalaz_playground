package org.neilconcepts

import scalaz._; import Scalaz._
import org.scalameter._

object Ternany extends App {

  private def init {
    val time = measure {
      for (i <- 1 |-> 10000) {
        val a = i % 2 == 0
        val x = a ? 1 | 0
      }
    }
    println(s"Total time: $time")

    val time2 = measure {
      for (i <- 1 |-> 10000) {
        val a = i % 2 == 0
        val x =
          if(a) 0
          else 1
      }
    }
    println(s"Total time: $time2")
  }

  init
}

