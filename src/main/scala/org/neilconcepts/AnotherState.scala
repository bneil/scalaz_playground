package org.neilconcepts

import scalaz._
import Scalaz._

/**
 * Use the state monad to 'process a trade' and store the new trade.
 * As well as processing the trade, handle validation errors.
 *
 * upgraded to scalaz 7.0 from
 * https://gist.github.com/channingwalton/2846428
 */
object AnotherState extends App {

  case class Trade(info: String)
  type Store = List[Trade]

  // a function that takes a new trade and processes it yielding new state and validation
  val newTrade = (newTrade: Trade) =>
    for (
      _ <- log("New Trade");
      accepted <- accept(newTrade);
      _ <- log("Hedging New Trade");
      hedged <- hedge(newTrade);
      _ <- log("Validating portfolio");
      portfolio <- validatePortfolio;
      _ <- log("New trade processed")
    ) yield (accepted.toValidationNel |@| hedged.toValidationNel |@| portfolio.toValidationNel) {_ + _ + _}

  println("Haven't done anything yet!")

  // assume some existing state
  var globalState: Store = Nil

  // exercise the newTrade function with the existing state
  val (newState, validation) = newTrade(Trade("Big Trade"))(globalState)

  // assign the new state to our global state if the validation says its ok
  globalState = validation.fold(failures => { println(failures); globalState }, msg => newState)

  println("Store contains " + globalState)

  // does nothing but print a messages and return the state its given
  def log(m: String) = State[Store, Unit](s => (s, println(m)))

  // accepts a trade putting it into the store
  def accept(newTrade: Trade) = State[Store, Validation[String, String]](s => (newTrade :: s, "trade accepted".success))

  // hedge against the new trade - apparently its all the rage
  def hedge(against: Trade) = State[Store, Validation[String, String]](s => (Trade("Hedge Trade against " + against) :: s, "hedge trade step".success))

  // validate the portfolio doing nothing with the state
  def validatePortfolio = State[Store, Validation[String, String]](s => {
    if (s.size > 10) (s, "Portolio is too big".failure)
    else (s, "All ok".success)
  })
}
