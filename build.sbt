lazy val coreSettings = Seq(
  organization := "com.sandinh",
  version := "1.0.0",
  scalaVersion := "2.11.8",
  crossScalaVersions := Seq("2.11.8", "2.12.0-M4"),

  scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-feature", "-target:jvm-1.8"),
  scalacOptions ++= (CrossVersion.scalaApiVersion(scalaVersion.value) match {
    case Some((2, 11)) => Seq("-Ybackend:GenBCode")
    case _ => Nil
  })
)

lazy val `sd-test-util` = project
  .settings(coreSettings: _*)

lazy val `sd-util` = project
  .dependsOn(`sd-test-util` % Test)
  .settings(coreSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe"  % "config"        % "1.3.0",
      "commons-codec" % "commons-codec" % "1.10"
    ).map(_ % Optional),

    libraryDependencies ++= Seq(
      "org.specs2"    %% "specs2-core"  % "3.8.4"
    ).map(_ % Test)
  )

lazy val root = project.in(file("."))
  .aggregate(`sd-test-util`, `sd-util`)
