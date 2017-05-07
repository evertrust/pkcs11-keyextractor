import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "fr.itassets",
      scalaVersion := "2.12.2",
      version      := "1.0"
    )),
    name := "PKCS#11 Key Extractor",
    libraryDependencies ++= Seq(scopt, bcProv)
  )
