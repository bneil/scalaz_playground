import sbt._
import sbt.Keys._

object Scalaz_playgroundBuild extends Build {

  lazy val scalaz_playground = Project(
    id = "scalaz_playground",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "scalaz_playground",
      organization := "org.example",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.11.7"
      // add other settings here
    )
  )
}
