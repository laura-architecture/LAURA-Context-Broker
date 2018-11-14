name := """lcb"""

version := "0.1.0"

lazy val root = (project in file(".")).enablePlugins(PlayMinimalJava)

scalaVersion := "2.12.6"

crossScalaVersions := Seq("2.11.12", "2.12.4")

libraryDependencies ++= Seq(
  ws,
  guice,
  "com.h2database" % "h2" % "1.4.197",
  "org.drools" % "drools-core" % "7.4.1.Final",
  "org.drools" % "drools-compiler" % "7.4.1.Final",
  "br.ufes.inf.lprm" % "scene-core" % "0.10.8-rc1" exclude("org.slf4j","slf4j-log4j12"),
  // Testing libraries for dealing with CompletionStage...
  "org.assertj" % "assertj-core" % "3.8.0" % Test,
  "org.awaitility" % "awaitility" % "3.0.0" % Test
)

dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-annotations" % "2.9.7"
dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-core" % "2.9.7"
dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-databind" % "2.9.7"

PlayKeys.devSettings += "play.server.http.idleTimeout" -> "infinite"

//Docker
packageName  := "registry.gitlab.com/laura-arch/lcb"
//packageName  := "evologica/competify.server"
dockerBaseImage := "anapsix/alpine-java:8"
version in Docker := version.value
maintainer := "isaacpereira@evologica.com.br"
dockerExposedPorts := Seq(9000, 9443)
dockerEntrypoint := Seq(s"bin/${name.value}", "-Dplay.server.http.idleTimeout=infinite")


// Make verbose tests
testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
