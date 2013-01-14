import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "nj1k"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      javaCore, javaJdbc, javaEbean,
      "mysql" % "mysql-connector-java" % "5.1.18",
      "net.sf.flexjson" % "flexjson" % "2.1" withSources() withJavadoc(),
      "org.jsoup" % "jsoup" % "1.6.3" withSources() withJavadoc(),
      "com.google.guava" % "guava" % "12.0.1" withSources() withJavadoc(),
      "commons-codec" % "commons-codec" % "1.4",
      "org.reflections" % "reflections" % "0.9.8",
      "com.typesafe" %% "play-plugins-mailer" % "2.1-RC2",
      "be.objectify" %% "deadbolt-java" % "2.0-SNAPSHOT",
      "org.apache.shiro" % "shiro-core" % "1.2.1" withSources() withJavadoc()
    )
        
    val main = play.Project(appName, appVersion, appDependencies).settings(
    		resolvers += "Slf4j Repository" at "http://repo2.maven.org/maven2/org/slf4j/",
    		resolvers += Resolver.url("Objectify Play Repository", url("http://schaloner.github.com/releases/"))(Resolver.ivyStylePatterns),
    		resolvers += Resolver.url("Objectify Play Snapshot Repository", url("http://schaloner.github.com/snapshots/"))(Resolver.ivyStylePatterns)
    )
}