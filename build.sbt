name := "ArgumentParse"
version := "0.0.1"
scalaVersion := "2.13.1"
autoScalaLibrary := false
description := "A library for parsing CLI type arguments."

val devs = List(
  Developer(id = "xaanit",
    name = "Jacob Frazier",
    email = "shadowjacob1@gmail.com",
    url = new URL("https://www.xaan.it"))
)

lazy val java = createProject(id = "java", settings = Seq(moduleName := "argumentParse-java", fork := true))

lazy val all = createProject(id = "all", file = Some("."), settings = Seq(moduleName := "argumentParse-all", fork := true))
  .dependsOn(java)
  .aggregate(java)

val commonSettings = Seq(
  autoScalaLibrary := false, // We don't want people using this to auto have the scala SDK
  version := "1.1.1",
  developers := devs,
  startYear := Some(2020),
  homepage := Some(new URL("https://github.com/xaanit/ArgumentParse")),
  libraryDependencies ++= Seq(
    "com.novocode" % "junit-interface" % "0.11" % "test",
    "junit" % "junit" % "4.13" % "test",
    "com.google.code.findbugs" % "jsr305" % "3.0.2"
  ),
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
    else Some("releases" at nexus + "service/local/staging/deploy/maven2")
  },
  testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-v", "-s", "--summary=2")
)

def createProject(
                   id: String,
                   file: Option[String] = None,
                   settings: Seq[SettingsDefinition] = Seq(),
                 ): Project =
  Project(id = id, base = sbt.file(file match {
    case Some(value) => value
    case None => id
  }))
    .settings(commonSettings ++ settings: _*)