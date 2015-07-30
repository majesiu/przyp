name := """przyp"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  specs2 % Test,
  "com.typesafe.play" %% "anorm" % "2.4.0",
  "jp.t2v" %% "play2-auth" % "0.14.0",
  "jp.t2v" %% "play2-auth-social" % "0.14.0", // for social login
  "jp.t2v" %% "play2-auth-test" % "0.14.0" % "test",
  "com.typesafe.play" %% "play-mailer" % "3.0.1"
)


resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
