name := """nj1k2"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "mysql" % "mysql-connector-java" % "5.1.18",
  "net.sf.flexjson" % "flexjson" % "2.1" withSources() withJavadoc(),
  "com.typesafe.play.plugins" %% "play-plugins-mailer" % "2.3.0",
  "com.google.guava" % "guava" % "12.0.1" withSources() withJavadoc(),
  "commons-codec" % "commons-codec" % "1.4",
  "org.jsoup" % "jsoup" % "1.6.3" withSources() withJavadoc(),
  "commons-io" % "commons-io" % "2.4",
  "org.reflections" % "reflections" % "0.9.8",
  "org.apache.shiro" % "shiro-core" % "1.2.1" withSources() withJavadoc(),
  "de.l3s.boilerpipe" % "boilerpipe" % "1.1.0" withSources() withJavadoc(),
  "net.sourceforge.nekohtml" % "nekohtml" % "1.9.13",
  "edu.stanford.nlp" % "stanford-corenlp" % "3.3.1" withSources() withJavadoc()
)
