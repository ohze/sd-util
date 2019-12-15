lazy val coreSettings = Seq(
  organization := "com.sandinh",
  version := "1.2.0-SNAPSHOT",
  scalaVersion := "2.13.1",
  crossScalaVersions := Seq("2.11.12", "2.12.10", "2.13.1"),

  scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-feature"),
  scalacOptions ++= (CrossVersion.scalaApiVersion(scalaVersion.value) match {
    case Some((2, 11)) => Seq("-Ybackend:GenBCode", "-target:jvm-1.8")
    case Some((2, 12)) => Seq("-target:jvm-1.8")
    case _ => Nil
  })
)

lazy val `env-hack` = project
  .settings(coreSettings: _*)

lazy val `sd-util` = project
  .dependsOn(`env-hack` % Test)
  .settings(coreSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "javax.inject"  % "javax.inject"  % "1",
      "com.typesafe"  % "config"        % "1.3.2",
      "commons-codec" % "commons-codec" % "1.11"
    ),

    libraryDependencies ++= Seq(
      "org.specs2"    %% "specs2-core"  % "3.9.5"
    ).map(_ % Test)
  )

lazy val `sd-util-root` = project.in(file("."))
  .settings(coreSettings: _*)
  .settings(publishArtifact := false)
  .aggregate(`env-hack`, `sd-util`)
