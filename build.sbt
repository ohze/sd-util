import com.sandinh.sbtsd.SdPlugin.javaVersion
import com.typesafe.tools.mima.core.{
  DirectMissingMethodProblem,
  MissingClassProblem,
  ProblemFilters
}

// same as versionPolicyPreviousVersions key from sbt-version-policy
val mimaPrevVersions = settingKey[Seq[String]](
  "Previous versions to check compatibility against."
)
val mimaPrevSettings = Seq(
  mimaPreviousArtifacts := mimaPrevVersions.value.toSet.map { v =>
    "com.sandinh" %% moduleName.value % v
  },
  mimaPrevVersions := (scalaBinaryVersion.value match {
    case "3"    => Nil
    case "2.13" => Seq("1.2.0")
    // sd-util 1.0.0 don't release for scala 2.12
    case "2.12" => Seq("1.0.1", "1.0.2", "1.0.3", "1.1.0", "1.2.0")
    case _      => Seq("1.0.0", "1.0.1", "1.0.2", "1.0.3", "1.1.0", "1.2.0")
  }),
)

val commonsCodecV = scalaBinaryVersion {
  // to keep compat with sd-util 1.0.0
  case "2.11" | "2.12" => "1.10"
  case _               => "1.15"
}
val configV = scalaBinaryVersion {
  // to keep compat with sd-util 1.0.0
  case "2.11" | "2.12" => "1.3.4"
  case _               => "1.4.1"
}

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
      "javax.inject" % "javax.inject" % "1",
      "com.typesafe" % "config" % configV.value,
      "commons-codec" % "commons-codec" % commonsCodecV.value,
      "com.github.scopt" %% "scopt" % "4.0.1" % Test,
    ) ++ specs2("-core", "-scalacheck").value,

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
    mimaBinaryIssueFilters ++= Seq(
      // We added a deprecated type alias in `package object util`:
      //  type RandomIterHashSet[A] = scala.collection.mutable.rnd.HashSet[A]
      // But mima don't understand that
      ProblemFilters.exclude[MissingClassProblem]("sd.util.RandomIterHashSet"),
      // cause by: scalafix LeakingImplicitClassVal
      // underlying val in `implicit class .. extends AnyVal` should be private
      ProblemFilters
        .exclude[DirectMissingMethodProblem]("sd.util.package#DoSomeOps.it"),
    ),
    Test / run / mainClass := Some("sd.util.DoSomeOpsBench"),
    // EnvHack using `setAccessible` to reflect into a private field of System.getenv
    // which default is deny in java 16+
    addOpensForTest(
      List(
        "java.base/java.lang=ALL-UNNAMED",
        "java.base/java.util=ALL-UNNAMED",
      )
    ),
    // HmacSHA1 introduced in v1.0.3 by vinhbt can't be normally compiled in java > 8
    // We re-impl and test that the old and new ones have same behaviour
    Test / unmanagedSourceDirectories ++= {
      if (javaVersion > 8) Nil
      else Seq(sourceDirectory.value / "test/java-8")
    },
  )

lazy val `sd-util-root` = project
  .in(file("."))
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
