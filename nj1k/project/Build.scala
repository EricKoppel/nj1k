import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "nj1k"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      javaCore, javaJdbc, javaEbean, cache, filters,
      "mysql" % "mysql-connector-java" % "5.1.18",
      "net.sf.flexjson" % "flexjson" % "2.1" withSources() withJavadoc(),
      "org.jsoup" % "jsoup" % "1.6.3" withSources() withJavadoc(),
      "com.google.guava" % "guava" % "12.0.1" withSources() withJavadoc(),
      "commons-codec" % "commons-codec" % "1.4",
      "commons-io" % "commons-io" % "2.4",
      "org.reflections" % "reflections" % "0.9.8",
      "com.typesafe" %% "play-plugins-mailer" % "2.1-RC2",
      "org.apache.shiro" % "shiro-core" % "1.2.1" withSources() withJavadoc(),
      "de.l3s.boilerpipe" % "boilerpipe" % "1.1.0" withSources() withJavadoc(),
      "net.sourceforge.nekohtml" % "nekohtml" % "1.9.13",
      "edu.stanford.nlp" % "stanford-corenlp" % "3.3.1" withSources() withJavadoc()
    )
        
    val main = play.Project(appName, appVersion, appDependencies).settings(
    		
    		testOptions in Test ~= { args =>
	    		for {
	      			arg <- args
	      			val ta: Tests.Argument = arg.asInstanceOf[Tests.Argument]
	      			val newArg = if(ta.framework == Some(TestFrameworks.JUnit)) ta.copy(args = List.empty[String]) else ta
	    		} yield newArg
  			}
    )
}
