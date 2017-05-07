import Dependencies._

enablePlugins(JavaAppPackaging)

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

// Do not publish the documentation
doc in Compile <<= target.map(_ / "none")

// Do not publish the sources in the packages
publishArtifact in(Compile, packageSrc) := false

// Automatic Scala code format
scalariformSettings