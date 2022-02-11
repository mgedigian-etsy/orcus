import Dependencies._

lazy val orcus = project
  .in(file("."))
  .settings(publish / skip := true)
  .settings(
    inThisBuild(
      Seq(
        organization := "com.github.tkrs",
        homepage     := Some(url("https://github.com/tkrs/orcus")),
        licenses     := Seq("MIT License" -> url("http://www.opensource.org/licenses/mit-license.php")),
        developers := List(
          Developer(
            "tkrs",
            "Takeru Sato",
            "type.in.type@gmail.com",
            url("https://github.com/tkrs")
          )
        ),
        scalaVersion       := V.`scala2.13`,
        crossScalaVersions := Seq(V.`scala2.13`),
        fork               := true,
        scalafmtOnCompile  := true,
        scalafixOnCompile  := true,
        scalafixDependencies += OrganizeImports,
        semanticdbEnabled := true,
        semanticdbVersion := scalafixSemanticdb.revision
      )
    )
  )
  .settings(
    Compile / console / scalacOptions --= warnCompilerOptions,
    Compile / console / scalacOptions += "-Yrepl-class-based"
  )
  .aggregate(core,
             bigtable,
             `cats-effect`,
             `twitter-util`,
             `bigtable-example`,
  )
  .dependsOn(core,
             bigtable,
             `cats-effect`,
             `twitter-util`,
             `bigtable-example`,
  )

lazy val core = project
  .in(file("modules/core"))
  .settings(sharedSettings)
  .settings(crossVersionSharedSources)
  .settings(
    description := "orcus core",
    moduleName  := "orcus-core"
  )
  .settings(
    libraryDependencies ++= Seq
      .concat(
        Seq(
          CatsCore,
          Shapeless,
          Java8Compat
        )
      )
      .map(_.withSources)
  )

lazy val `twitter-util` = project
  .in(file("modules/twitter-util"))
  .settings(sharedSettings)
  .settings(crossVersionSharedSources)
  .settings(
    description := "orcus twitter-util",
    moduleName  := "orcus-twitter-util"
  )
  .settings(
    libraryDependencies += TwitterUtil.withSources
  )
  .dependsOn(core % "compile->compile;test->test")

lazy val `cats-effect` = project
  .in(file("modules/cats-effect"))
  .settings(sharedSettings)
  .settings(
    description := "orcus cats-effect",
    moduleName  := "orcus-cats-effect"
  )
  .settings(
    libraryDependencies += CatsEffect.withSources
  )
  .dependsOn(core % "compile->compile;test->test")

lazy val bigtable = project
  .in(file("modules/bigtable"))
  .settings(sharedSettings)
  .settings(crossVersionSharedSources)
  .settings(
    description := "orcus bigtable",
    moduleName  := "orcus-bigtable"
  )
  .settings(
    libraryDependencies += Bigtable.withSources
  )
  .dependsOn(core)

lazy val `bigtable-example` = project
  .in(file("modules/bigtable-example"))
  .settings(sharedSettings)
  .settings(publish / skip := true)
  .settings(crossVersionSharedSources)
  .settings(
    description := "orcus bigtable-example",
    moduleName  := "orcus-bigtable-example"
  )
  .settings(
    libraryDependencies ++= Seq(
      Logging,
      LogbackClassic
    ).map(_.withSources)
  )
  .settings(
    fork            := true,
    coverageEnabled := false
  )
  .settings(
    scalacOptions -= "-Xfatal-warnings"
  )
  .dependsOn(bigtable, `cats-effect`)

lazy val compilerOptions = Seq(
  "-deprecation",
  "-encoding",
  "utf-8",
  "-explaintypes",
  "-feature",
  "-language:higherKinds",
  "-unchecked"
)

lazy val warnCompilerOptions = Seq(
  // "-Xlint",
  "-Xcheckinit",
  // "-Xfatal-warnings",
  "-Ywarn-unused:_",
  "-Ywarn-extra-implicit",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen"
)

lazy val obsoletedOptions = Seq("-Xfuture", "-Ypartial-unification", "-Yno-adapted-args", "-Ywarn-inaccessible")

lazy val sharedSettings = Seq(
  scalacOptions ++= compilerOptions ++ warnCompilerOptions ++ {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, n)) if n >= 13 => Nil
      case _                       => obsoletedOptions
    }
  },
  libraryDependencies ++= TestDeps ++ Seq(compilerPlugin(KindProjector))
)

lazy val crossVersionSharedSources: Seq[Setting[_]] =
  Seq(Compile, Test).map { sc =>
    (sc / unmanagedSourceDirectories) ++= {
      (sc / unmanagedSourceDirectories).value.flatMap { dir =>
        if (dir.getName != "scala") Seq(dir)
        else
          CrossVersion.partialVersion(scalaVersion.value) match {
            case Some((2, n)) if n >= 13 => Seq(file(dir.getPath + "_2.13+"))
            case _                       => Seq(file(dir.getPath + "_2.12-"))
          }
      }
    }
  }
