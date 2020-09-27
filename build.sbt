name := "HW1_CloudSim"

version := "0.1"

scalaVersion := "2.13.3"

mainClass in (Compile, run) := Some("Main")
//mainClass := Some("Main")

libraryDependencies ++= Seq(
  //Typesafe configuration
  "com.typesafe" % "config" % "1.4.0",
  // CloudSim Plus
  "org.cloudsimplus" % "cloudsim-plus" % "4.3.4",
  // Logback logging framework
 // "ch.qos.logback" % "logback-classic" % "1.2.3",
  "org.slf4j" % "slf4j-log4j12" % "1.7.28",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "org.gnieh" % "logback-config" % "0.3.1"
)