import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "nj1k"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
        
      "mysql" % "mysql-connector-java" % "5.1.18",
      "net.sf.flexjson" % "flexjson" % "2.1" withSources() withJavadoc(),
      "org.jsoup" % "jsoup" % "1.6.3" withSources() withJavadoc(),
      "com.google.guava" % "guava" % "12.0.1" withSources() withJavadoc(),
      "org.apache.shiro" % "shiro-core" % "1.2.1" withSources() withJavadoc(),
      "org.apache.shiro" % "shiro-play" % "1.2.1",
      "org.apache" %% "shiro-play-templates" % "1.0-SNAPSHOT",
      "commons-codec" % "commons-codec" % "1.4",
      "org.reflections" % "reflections" % "0.9.8"
    )
        
    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
    		resolvers += "Slf4j Repository" at "http://repo2.maven.org/maven2/org/slf4j/",
    		resolvers += "Local Maven Repository" at "file://"+Path.userHome+"/.m2/repository"
    )
}
