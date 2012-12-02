import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "play2-docs-renderer"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm,
    "org.pegdown" % "pegdown" % "1.2.0"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
	  // add a resources directory for the test scope
      resourceDirectory in Test <<= baseDirectory / "test/resources"
  )

}
