name := "HW1_CloudSim"

version := "0.1"

scalaVersion := "2.13.3"

mainClass in (Compile, run) := Some("Main")
//mainClass := Some("Main")
ThisBuild / useCoursier := false

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.4.0",  //Typesafe configuration
  "org.cloudsimplus" % "cloudsim-plus" % "4.3.4", // CloudSim Plus
  "org.slf4j" % "slf4j-log4j12" % "1.7.28", // Logback logging framework
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "org.gnieh" % "logback-config" % "0.3.1",
  "junit" % "junit" % "4.13"  // JUnit Testing
)