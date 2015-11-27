lazy val commonSettings = Seq(
  organization := "com.kromm",
  version := "0.0.1",
  scalaVersion := "2.11.4"
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "Launcher"
  )

libraryDependencies += "org.scalafx" %% "scalafx" % "8.0.60-R9"
libraryDependencies += "org.scala-lang" % "scala-swing" % "2.11.0-M7"
