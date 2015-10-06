package org.neilconcepts

import scalaz._; import Scalaz._
import org.scalameter._
import java.util.Random

object ScalazState extends App {
  def dice() = State[Random, Int](r => (r, r.nextInt(6)+1))

  private def init {
    def TwoDice() = for {
      r1 <- dice()
      r2 <- dice()
    } yield (r1, r2)

    val demRolls = List.fill(10)(TwoDice())
    type StateRandom[x] = State[Random, x]
    val rollz = demRolls.sequence[StateRandom, (Int, Int)]
    val dblDown = rollz.eval(new Random(1L))

    println(dblDown)

  }

  init
}
