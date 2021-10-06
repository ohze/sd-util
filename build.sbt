lazy val coreSettings = Seq(
  scalaVersion := scala213,
  crossScalaVersions := Seq(scala211, scala212, scala213),
)

lazy val `env-hack` = project.settings(coreSettings)

lazy val `sd-util` = project
  .dependsOn(`env-hack` % Test)
  .settings(
    coreSettings,
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.3",
      "javax.inject"  % "javax.inject"  % "1",
      "com.typesafe"  % "config"        % "1.4.0",
      "commons-codec" % "commons-codec" % "1.13",
      "org.specs2"    %% "specs2-core"  % "4.8.1" % Test,
      "com.github.scopt" %% "scopt" % "4.0.0-RC2" % Test,
    ),

    // Adds a `src/main/scala-2.13+` source directory for Scala 2.13 and newer
    // and a `src/main/scala-2.13-` source directory for Scala version older than 2.13
    Compile / unmanagedSourceDirectories += {
      val sourceDir = (Compile / sourceDirectory).value
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, n)) if n >= 13 => sourceDir / "scala-2.13+"
        case _                       => sourceDir / "scala-2.13-"
      }
    },

    Test / run / mainClass := Some("sd.util.DoSomeOpsBench"),
  )

lazy val `sd-util-root` = project.in(file("."))
  .settings(coreSettings, skipPublish)
  .aggregate(`env-hack`, `sd-util`)

inThisBuild(
  Seq(
    versionScheme := Some("semver-spec"),
    developers := List(
      Developer(
        "thanhbv",
        "Bui Viet Thanh",
        "thanhbv@sandinh.net",
        url("https://sandinh.com")
      )
    )
  )
)
