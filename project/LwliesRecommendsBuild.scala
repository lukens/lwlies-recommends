import sbt._
import sbt.Keys._

object LwliesRecommendsBuild extends Build {

  lazy val lwliesRecommends = Project(
    id = "lwlies-recommends",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "lwlies recommends",
      organization := "com.me.lukens",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.10.2",
      // add other settings here
      libraryDependencies ++= Seq(
        "net.sourceforge.htmlcleaner" % "htmlcleaner" % "2.7",
        "com.github.tototoshi" %% "scala-csv" % "0.8.0"
      )
    )
  )
}
