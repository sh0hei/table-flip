name := """table-flip"""

version := "1.0"

scalaVersion := "2.11.8"

// Change this to another test framework if you prefer
libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % "2.11.8",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "org.specs2" %% "specs2-core" % "3.7.2" % "test"
)

scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked", "-Xlint")

scalacOptions in Test ++= Seq("-Yrangepos")

// Uncomment to use Akka
//libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.11"

