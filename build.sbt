lazy val coreSettings = Seq(
  organization := "com.sandinh",
  version := "1.0.1",
  scalaVersion := "2.12.3",
  crossScalaVersions := Seq("2.11.11", "2.12.3"),

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
      "javax.inject"  % "javax.inject"  % "1",
      "com.typesafe"  % "config"        % "1.3.1",
      "commons-codec" % "commons-codec" % "1.10"
    ).map(_ % Optional),

    libraryDependencies ++= Seq(
      "org.specs2"    %% "specs2-core"  % "3.9.5"
    ).map(_ % Test)
  )

lazy val root = project.in(file("."))
  .settings(coreSettings: _*)
  .settings(publishArtifact := false)
  .aggregate(`sd-test-util`, `sd-util`)
