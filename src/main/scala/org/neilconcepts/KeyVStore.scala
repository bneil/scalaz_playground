package org.neilconcepts

import scalaz._; import Scalaz._
import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global
import Kleisli._

object KeyVStore extends App{
  sealed trait KVS[Next]
  case class Put[Next](key: String, value: String, next: Next) extends KVS[Next]
  case class Delete[Next](key: String, next: Next) extends KVS[Next]
  case class Get[Next](key: String, onValue: String => Next) extends KVS[Next]

  new Functor[KVS] {
    def map[A, B](kvs: KVS[A])(f: A => B): KVS[B] = kvs match {
      case Put(key, value, next) => Put(key, value, f(next))
      case Delete(key, next) => Delete(key, f(next))
      case Get(key, onResult) => Get(key, onResult andThen f)
    }
  }
}
