import sbt._

object Dependencies {
  val Ver = new {
    val `scala2.13`      = "2.13.1"
    val `scala2.12`      = "2.12.10"
    val cats             = "2.0.0"
    val fs2              = "2.0.1"
    val monix            = "3.1.0"
    val twitterUtil      = "19.11.0"
    val catsEffect       = "2.0.0"
    val shapeless        = "2.3.3"
    val scalacheck       = "1.14.2"
    val scalatest        = "3.0.8"
    val mockito          = "2.23.0"
    val kindProjector    = "0.11.0"
    val hbase            = "2.1.4"
    val bigtableHBase    = "1.12.1"
    val bigtable         = "1.7.1"
    val logback          = "1.2.3"
    val scalaLogging     = "3.9.2"
    val scalaJava8Compat = "0.9.0"
  }

  val Pkg = new {
    lazy val catsCore       = "org.typelevel"              %% "cats-core"            % Ver.cats
    lazy val catsFree       = "org.typelevel"              %% "cats-free"            % Ver.cats
    lazy val catsEffect     = "org.typelevel"              %% "cats-effect"          % Ver.catsEffect
    lazy val fs2Core        = "co.fs2"                     %% "fs2-core"             % Ver.fs2
    lazy val monixEval      = "io.monix"                   %% "monix-eval"           % Ver.monix
    lazy val twitterUtil    = "com.twitter"                %% "util-core"            % Ver.twitterUtil
    lazy val catbirdUtil    = "io.catbird"                 %% "catbird-util"         % Ver.twitterUtil
    lazy val shapeless      = "com.chuusai"                %% "shapeless"            % Ver.shapeless
    lazy val java8Compat    = "org.scala-lang.modules"     %% "scala-java8-compat"   % Ver.scalaJava8Compat
    lazy val scalacheck     = "org.scalacheck"             %% "scalacheck"           % Ver.scalacheck
    lazy val mockito        = "org.mockito"                % "mockito-core"          % Ver.mockito
    lazy val hbase          = "org.apache.hbase"           % "hbase-client"          % Ver.hbase
    lazy val bigtableHBase  = "com.google.cloud.bigtable"  % "bigtable-hbase-2.x"    % Ver.bigtableHBase
    lazy val bigtable       = "com.google.cloud"           % "google-cloud-bigtable" % Ver.bigtable
    lazy val logbackClassic = "ch.qos.logback"             % "logback-classic"       % Ver.logback
    lazy val logging        = "com.typesafe.scala-logging" %% "scala-logging"        % Ver.scalaLogging
    lazy val scalatest      = "org.scalatest"              %% "scalatest"            % Ver.scalatest

    lazy val kindProjector = ("org.typelevel" %% "kind-projector" % Ver.kindProjector).cross(CrossVersion.full)

    lazy val forTest = Seq(scalatest, scalacheck, mockito).map(_ % Test)
  }
}
