seq(Revolver.settings: _*)

resolvers ++= Seq(
 "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/releases",
  "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/"
)

testFrameworks += new TestFramework("org.scalameter.ScalaMeterFramework")

parallelExecution in Test := false

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.1.4",
  "org.scalacheck" %% "scalacheck" % "1.12.5" % "test",
  "org.typelevel" %% "shapeless-scalacheck" % "0.4",
  "com.github.scala-blitz" %% "scala-blitz" % "1.1",
  "com.storm-enroute" %% "scalameter" % "0.7",
  "com.storm-enroute" %% "scalameter-core" % "0.7"
)

initialCommands in console := "import scalaz._, Scalaz._"
