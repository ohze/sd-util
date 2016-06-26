organization := "com.sandinh"
name := "sd-util"

version := "1.0.0"

scalaVersion := "2.11.8"
crossScalaVersions := Seq("2.11.8", "2.12.0-M4")

scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-feature", "-target:jvm-1.8")
scalacOptions ++= (CrossVersion.scalaApiVersion(scalaVersion.value) match {
  case Some((2, 11)) => Seq("-Ybackend:GenBCode")
  case _ => Nil
})

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.3.0"
)
