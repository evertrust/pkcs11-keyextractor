import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "fr.itassets",
      scalaVersion := "2.12.2",
      version      := "0.9"
    )),
    name := "PKCS#11 ",
    libraryDependencies ++= Seq(scopt, bcProv)
  )
