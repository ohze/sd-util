
// same as versionPolicyPreviousVersions key from sbt-version-policy
val mimaPrevVersions = settingKey[Seq[String]](
  "Previous versions to check compatibility against."
)
val mimaPrevSettings = Seq(
  mimaPreviousArtifacts := mimaPrevVersions.value.toSet.map { v =>
    "com.sandinh" %% moduleName.value % v
  },
  mimaPrevVersions := (scalaBinaryVersion.value match {
    case "3" => Nil
    case "2.13" => Seq("1.2.0")
    // sd-util 1.0.0 don't release for scala 2.12
    case "2.12" => Seq("1.0.1", "1.0.2", "1.0.3", "1.1.0", "1.2.0")
    case _ => Seq("1.0.0", "1.0.1", "1.0.2", "1.0.3", "1.1.0", "1.2.0")
  }),
)

lazy val coreSettings = Seq(
  scalaVersion := scala213,
  crossScalaVersions := Seq(scala211, scala212, scala213, scala3),
)

lazy val `env-hack` = project.settings(
  coreSettings,
  mimaPrevSettings,
  mimaPrevVersions -= "1.0.0", // don't have this version
)

lazy val `sd-util` = project
  .dependsOn(`env-hack` % Test)
  .settings(
    coreSettings,
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.5.0",
      "javax.inject"  % "javax.inject"  % "1",
      "com.typesafe"  % "config"        % "1.4.0",
      "commons-codec" % "commons-codec" % "1.13",
      "com.github.scopt" %% "scopt" % "4.0.1" % Test,
    ) ++ specs2("-core").value,

    // Adds a `src/main/scala-2.13+` source directory for Scala 2.13 and newer
    // and a `src/main/scala-2.13-` source directory for Scala version older than 2.13
    Compile / unmanagedSourceDirectories += {
      val sourceDir = (Compile / sourceDirectory).value
      scalaBinaryVersion.value match {
        case "2.12" | "2.11" => sourceDir / "scala-2.13-"
        case _               => sourceDir / "scala-2.13+"
      }
    },

    mimaPrevSettings,

    Test / run / mainClass := Some("sd.util.DoSomeOpsBench"),
    // EnvHack using `setAccessible` to reflect into a private field of System.getenv
    // which default is deny in java 16+
    addOpensForTest(
      List(
        "java.base/java.lang=ALL-UNNAMED",
        "java.base/java.util=ALL-UNNAMED",
      )
    ),
  )

lazy val `sd-util-root` = project.in(file("."))
  .disablePlugins(MimaPlugin)
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
