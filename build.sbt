seq(Revolver.settings: _*)

resolvers += "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/"

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.1.4",
  "org.scalacheck" %% "scalacheck" % "1.12.5" % "test",
  "org.typelevel" %% "shapeless-scalacheck" % "0.4",
  "com.github.scala-blitz" %% "scala-blitz" % "1.1"
)

initialCommands in console := "import scalaz._, Scalaz._"
