package org.neilconcepts

import scalaz._
import Scalaz._
import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global
import Kleisli._

object Twitter {
  def getTweets(name: String, apiKey: String, apiSecret: String): Future[List[Tweet]] = future {
    val tweet1 = Tweet(UserDetails("test"), "first tweet")
    val tweet2 = Tweet(UserDetails("test"), "second tweet")
    val tweet3 = Tweet(UserDetails("test"), "third tweet")
    List(tweet1, tweet2, tweet3)
  }

  def getTweetsOld(username: String): Config => Future[List[Tweet]] = c => {
    Twitter.getTweets(username, c.key, c.secret)
  }

  def getTweets(username: String): ReaderT[Future, Config, List[Tweet]] = {
    kleisli { c =>
      getTweets(username, c.key, c.secret)
    }
  }

  case class UserDetails(screenName: String)
  case class Tweet(user: UserDetails, content: String)
}

object Neil {
  def generateString(text: String, context: Int): Future[String] = future {
    text.reverse
  }

  def generateStringOld(text: String): Config => Future[String] = c => {
    generateString(text, c.context)
  }

  def generateString(text: String): ReaderT[Future, Config, String] = {
    Kleisli { c =>
      Neil.generateString(text, c.context)
    }
  }
}

object Actions { /*//{*/
  def simpleActions() = { /*//{*/
    def tweetsFuture =
      Twitter.getTweets("test", "key", "secret")

    def tweets = Await.result(tweetsFuture, 1.second)
    println(tweets)

    def randomString = Neil.generateString(
      tweets.map(_.content).mkString(" "), 2)

    val meh = Await.result(randomString, 1.second)
    println(s"test: $meh")
  } /*//}*/

  def monadActions() = { /*//{*/
    val apiKey = "key"
    val apiSec = "secret"
    val context = 2

    def randString(username: String): Future[String] = for {
      ts <- Twitter.getTweets(username, apiKey, apiSec)
      tweetsAsText = ts.map(_.content).mkString(" ")
      r <- Neil.generateString(tweetsAsText, context)
    } yield r

    val a = Await.result(randString("test"), 1.second)
    println(a)
  } /*//}*/

  def testUtils() { /*//{*/
    val config = Config("test", "secret", 2)
    println(Util.isEvenLengthString("test"))
    println(Util.isEvenLengthString("hello"))

    val a = Util.keyAndSecret(config)
    println(a)
  } /*//}*/

  def monadActions2() = { /*//{*/
    val config = Config("test", "secret", 2)
    val tweets = for {
      tweet <- Twitter.getTweets("test")(config)
    } yield tweet

    println(tweets)
  } /*//}*/

  def monadTrans() = {
    val fo: Future[Option[Int]] = Future(Some(1))
    val optionT: OptionT[Future, Int] = OptionT(fo)
    val foUpdated = optionT.map(_ + 9).run

    println(Await.result(foUpdated, 1.second) === Some(10))

    // ---- part 2
    val f: String => Future[Int] = s => Future(s.length)
    val reader: ReaderT[Future, String, Int] = kleisli(f)
    val futResult: Future[Int] = reader.run("testing")

    println(Await.result(futResult, 1.second))
  }
} /*//}*/

object Util { /*//{*/
  val strLen: String => Int = _.length
  val isEven: Int => Boolean = _ % 2 == 0
  val isEvenLengthString: String => Boolean = strLen andThen isEven

  val keyFromConfig: Config => String = _.key
  val secretFromConfig: Config => String = _.secret

  val keyAndSecretOld: Config => (String, String) = a =>
    (keyFromConfig(a), secretFromConfig(a))

  val keyAndSecret: Config => (String, String) =
    for {
      k <- keyFromConfig
      s <- secretFromConfig
    } yield (k, s)

} /*//}*/

case class Config(key: String, secret: String, context: Int)

object Scalaz_playground extends App {
  //Actions.simpleActions()
  //Actions.monadActions()
  //Actions.testUtils()
  //Actions.monadActions2()
  Actions.monadTrans()
}
