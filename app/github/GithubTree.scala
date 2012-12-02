package github

import play.api.libs.json._
import scala.util.matching.Regex
import play.api.libs.functional.syntax._
   
object GithubTree {

  def toList(jsonValue: JsValue) = {

    (jsonValue \ "paths").as[List[String]]
  }

  def cleanPath(pathsToClean: List[String], link: String): Option[String] = {

    if (pathsToClean.contains("documentation/manual/" + link + ".md")) {
      Some("")
    } else {

      def pattern = ("documentation/manual([A-Za-z0-9/]+)/" + link + ".md").r
      val found = pathsToClean.flatMap(pathToClean => pattern.findFirstMatchIn(pathToClean) match {
        case Some(matching) => Some(matching.group(1))
        case None => None
      })
      if (found.size == 1)
        Some(found(0))
      else None
    }
  }

  def findPath(link: String): Option[String] = {

    val links = toList(Json.parse(fetchFileTreeAsJson)).filter(_.contains(link))
    cleanPath(links, link)
  }

  def fetchFileTreeAsJson() = {
    json
  }
  
  case class GithubTreeEntry(
      path: String
      )
  
  case class GithubTree(
      sha: String,
      tree: List[GithubTreeEntry],
      url: String)

  implicit val githubTreeEntryReads: Reads[GithubTreeEntry]  = Json.reads[GithubTreeEntry]
  
  implicit val githubTreeReads: Reads[GithubTree]  = (
  (__ \ "sha").read[String] and
  (__ \ "tree").read[List[GithubTreeEntry]] and
  (__ \ "url").read[String]
		  )(GithubTree)
  
  def parse(value: String): JsResult[GithubTree] = {
    Json.parse(value).validate[GithubTree]
  }

  //FIXME: use the Github API
  //https://api.github.com/repos/playframework/Play20/git/trees/46d04362930b6fbde655aac14c1f24881c6cd451?recursive=1
  
  // https://github.com/playframework/Play20/tree-list/794a9e550745165c9e7f7573799e6b75c030161c
  val json = """
  {
   "paths":[
      ".gitignore",
      "CONTRIBUTING.md",
      "README.md",
      "documentation/manual/Highlights.md",
      "documentation/manual/Home.md",
      "documentation/manual/Migration.md",
      "documentation/manual/Modules.md",
      "documentation/manual/User-Groups-around-the-World.md",
      "documentation/manual/_Sidebar.md",
      "documentation/manual/about/Philosophy.md",
      "documentation/manual/book/Book",
      "documentation/manual/book/BookDetailledTopics.md",
      "documentation/manual/book/BookHome.md",
      "documentation/manual/book/BookJavaHome.md",
      "documentation/manual/book/BookScalaHome.md",
      "documentation/manual/detailledTopics/assets/Assets.md",
      "documentation/manual/detailledTopics/assets/AssetsCoffeeScript.md",
      "documentation/manual/detailledTopics/assets/AssetsGoogleClosureCompiler.md",
      "documentation/manual/detailledTopics/assets/AssetsLess.md",
      "documentation/manual/detailledTopics/assets/RequireJS-support.md",
      "documentation/manual/detailledTopics/assets/_Sidebar.md",
      "documentation/manual/detailledTopics/assets/images/ClosureError.png",
      "documentation/manual/detailledTopics/assets/images/coffeeError.png",
      "documentation/manual/detailledTopics/assets/images/lessError.png",
      "documentation/manual/detailledTopics/build/Build.md",
      "documentation/manual/detailledTopics/build/SBTDependencies.md",
      "documentation/manual/detailledTopics/build/SBTSettings.md",
      "documentation/manual/detailledTopics/build/SBTSubProjects.md",
      "documentation/manual/detailledTopics/build/_Sidebar.md",
      "documentation/manual/detailledTopics/configuration/AkkaCore.md",
      "documentation/manual/detailledTopics/configuration/Configuration.md",
      "documentation/manual/detailledTopics/configuration/SettingsJDBC.md",
      "documentation/manual/detailledTopics/configuration/SettingsLogger.md",
      "documentation/manual/detailledTopics/configuration/_Sidebar.md",
      "documentation/manual/detailledTopics/database/Developing-with-the-H2-Database.md",
      "documentation/manual/detailledTopics/evolutions/Evolutions.md",
      "documentation/manual/detailledTopics/evolutions/images/evolutions.png",
      "documentation/manual/detailledTopics/evolutions/images/evolutionsError.png",
      "documentation/manual/detailledTopics/production/Deploying-CloudFoundry.md",
      "documentation/manual/detailledTopics/production/Deploying-to-CloudBees.md",
      "documentation/manual/detailledTopics/production/HTTPServer.md",
      "documentation/manual/detailledTopics/production/Production.md",
      "documentation/manual/detailledTopics/production/ProductionConfiguration.md",
      "documentation/manual/detailledTopics/production/ProductionDist.md",
      "documentation/manual/detailledTopics/production/ProductionHeroku.md",
      "documentation/manual/detailledTopics/production/_Sidebar.md",
      "documentation/manual/detailledTopics/production/images/dist.png",
      "documentation/manual/detailledTopics/production/images/stage.png",
      "documentation/manual/detailledTopics/production/images/start.png",
      "documentation/manual/gettingStarted/Anatomy.md",
      "documentation/manual/gettingStarted/IDE.md",
      "documentation/manual/gettingStarted/Installing.md",
      "documentation/manual/gettingStarted/NewApplication.md",
      "documentation/manual/gettingStarted/PlayConsole.md",
      "documentation/manual/gettingStarted/Samples.md",
      "documentation/manual/gettingStarted/images/comet-clock.png",
      "documentation/manual/gettingStarted/images/computerdatabase.png",
      "documentation/manual/gettingStarted/images/console.png",
      "documentation/manual/gettingStarted/images/consoleCompile.png",
      "documentation/manual/gettingStarted/images/consoleEval.png",
      "documentation/manual/gettingStarted/images/consoleRun.png",
      "documentation/manual/gettingStarted/images/eclipse.png",
      "documentation/manual/gettingStarted/images/errorPage.png",
      "documentation/manual/gettingStarted/images/forms.png",
      "documentation/manual/gettingStarted/images/helloworld.png",
      "documentation/manual/gettingStarted/images/idea.png",
      "documentation/manual/gettingStarted/images/play.png",
      "documentation/manual/gettingStarted/images/playNew.png",
      "documentation/manual/gettingStarted/images/rps-screenshot.png",
      "documentation/manual/gettingStarted/images/websocket-chat.png",
      "documentation/manual/gettingStarted/images/zentask.png",
      "documentation/manual/hacking/BuildingFromSource.md",
      "documentation/manual/hacking/CIServer.md",
      "documentation/manual/hacking/Guidelines.md",
      "documentation/manual/hacking/Issues.md",
      "documentation/manual/hacking/Repositories.md",
      "documentation/manual/hacking/_Sidebar.md",
      "documentation/manual/javaGuide/JavaHome.md",
      "documentation/manual/javaGuide/_Sidebar.md",
      "documentation/manual/javaGuide/main/akka/JavaAkka.md",
      "documentation/manual/javaGuide/main/async/JavaAsync.md",
      "documentation/manual/javaGuide/main/async/JavaComet.md",
      "documentation/manual/javaGuide/main/async/JavaStream.md",
      "documentation/manual/javaGuide/main/async/JavaWebSockets.md",
      "documentation/manual/javaGuide/main/async/_Sidebar.md",
      "documentation/manual/javaGuide/main/cache/JavaCache.md",
      "documentation/manual/javaGuide/main/forms/JavaFormHelpers.md",
      "documentation/manual/javaGuide/main/forms/JavaForms.md",
      "documentation/manual/javaGuide/main/forms/_Sidebar.md",
      "documentation/manual/javaGuide/main/global/JavaGlobal.md",
      "documentation/manual/javaGuide/main/global/JavaInterceptors.md",
      "documentation/manual/javaGuide/main/global/_Sidebar.md",
      "documentation/manual/javaGuide/main/http/JavaActions.md",
      "documentation/manual/javaGuide/main/http/JavaActionsComposition.md",
      "documentation/manual/javaGuide/main/http/JavaBodyParsers.md",
      "documentation/manual/javaGuide/main/http/JavaContentNegotiation.md",
      "documentation/manual/javaGuide/main/http/JavaResponse.md",
      "documentation/manual/javaGuide/main/http/JavaRouting.md",
      "documentation/manual/javaGuide/main/http/JavaSessionFlash.md",
      "documentation/manual/javaGuide/main/http/_Sidebar.md",
      "documentation/manual/javaGuide/main/http/images/routesError.png",
      "documentation/manual/javaGuide/main/i18n/JavaI18N.md",
      "documentation/manual/javaGuide/main/inject/JavaInjection.md",
      "documentation/manual/javaGuide/main/json/JavaJsonRequests.md",
      "documentation/manual/javaGuide/main/sql/JavaDatabase.md",
      "documentation/manual/javaGuide/main/sql/JavaEbean.md",
      "documentation/manual/javaGuide/main/sql/JavaJPA.md",
      "documentation/manual/javaGuide/main/sql/_Sidebar.md",
      "documentation/manual/javaGuide/main/sql/images/dbError.png",
      "documentation/manual/javaGuide/main/templates/JavaTemplateUseCases.md",
      "documentation/manual/javaGuide/main/templates/JavaTemplates.md",
      "documentation/manual/javaGuide/main/templates/_Sidebar.md",
      "documentation/manual/javaGuide/main/templates/images/templatesError.png",
      "documentation/manual/javaGuide/main/tests/JavaFunctionalTest.md",
      "documentation/manual/javaGuide/main/tests/JavaTest.md",
      "documentation/manual/javaGuide/main/tests/_Sidebar.md",
      "documentation/manual/javaGuide/main/upload/JavaFileUpload.md",
      "documentation/manual/javaGuide/main/ws/JavaOpenID.md",
      "documentation/manual/javaGuide/main/ws/JavaWS.md",
      "documentation/manual/javaGuide/main/ws/_Sidebar.md",
      "documentation/manual/javaGuide/main/xml/JavaXmlRequests.md",
      "documentation/manual/javaGuide/tutorials/todolist/JavaTodoList.md",
      "documentation/manual/javaGuide/tutorials/todolist/images/blank.png",
      "documentation/manual/javaGuide/tutorials/todolist/images/error.png",
      "documentation/manual/javaGuide/tutorials/todolist/images/evolutions.png",
      "documentation/manual/javaGuide/tutorials/todolist/images/filled.png",
      "documentation/manual/javaGuide/tutorials/todolist/images/hello.png",
      "documentation/manual/javaGuide/tutorials/todolist/images/new.png",
      "documentation/manual/javaGuide/tutorials/todolist/images/routes.png",
      "documentation/manual/javaGuide/tutorials/todolist/images/run.png",
      "documentation/manual/javaGuide/tutorials/todolist/images/todo.png",
      "documentation/manual/javaGuide/tutorials/todolist/images/welcome.png",
      "documentation/manual/javaGuide/tutorials/zentasks/JavaGuide1.md",
      "documentation/manual/javaGuide/tutorials/zentasks/JavaGuide2.md",
      "documentation/manual/javaGuide/tutorials/zentasks/JavaGuide3.md",
      "documentation/manual/javaGuide/tutorials/zentasks/JavaGuide4.md",
      "documentation/manual/javaGuide/tutorials/zentasks/JavaGuide5.md",
      "documentation/manual/javaGuide/tutorials/zentasks/JavaGuide6.md",
      "documentation/manual/javaGuide/tutorials/zentasks/_Sidebar.md",
      "documentation/manual/javaGuide/tutorials/zentasks/files/less-stylesheets.tar.gz",
      "documentation/manual/javaGuide/tutorials/zentasks/files/public-assets.tar.gz",
      "documentation/manual/javaGuide/tutorials/zentasks/files/test-data.yml",
      "documentation/manual/javaGuide/tutorials/zentasks/images/compileerror.png",
      "documentation/manual/javaGuide/tutorials/zentasks/images/dashboard1.png",
      "documentation/manual/javaGuide/tutorials/zentasks/images/dashboard2.png",
      "documentation/manual/javaGuide/tutorials/zentasks/images/dashboard3.png",
      "documentation/manual/javaGuide/tutorials/zentasks/images/dashboardloggedin.png",
      "documentation/manual/javaGuide/tutorials/zentasks/images/evolution.png",
      "documentation/manual/javaGuide/tutorials/zentasks/images/loginfail.png",
      "documentation/manual/javaGuide/tutorials/zentasks/images/mockup.png",
      "documentation/manual/javaGuide/tutorials/zentasks/images/new.png",
      "documentation/manual/javaGuide/tutorials/zentasks/images/welcome.png",
      "documentation/manual/javaGuide/tutorials/zentasks/images/zentasks.png",
      "documentation/manual/sandbox/Declare-anonymous-function-in-templates.md",
      "documentation/manual/sandbox/Javacaching.md",
      "documentation/manual/sandbox/Javacookies.md",
      "documentation/manual/sandbox/Javahttp.md",
      "documentation/manual/sandbox/Javajson.md",
      "documentation/manual/sandbox/Javaplayaslibrary.md",
      "documentation/manual/sandbox/Javaplugin.md",
      "documentation/manual/sandbox/Missing.md",
      "documentation/manual/sandbox/Scalacache.md",
      "documentation/manual/sandbox/Scalaextend.md",
      "documentation/manual/sandbox/Scalahttp.md",
      "documentation/manual/sandbox/Scalaplugin.md",
      "documentation/manual/sandbox/Status.md",
      "documentation/manual/sandbox/Tips.md",
      "documentation/manual/scalaGuide/ScalaHome.md",
      "documentation/manual/scalaGuide/_Sidebar.md",
      "documentation/manual/scalaGuide/advanced/iteratees/Enumeratees.md",
      "documentation/manual/scalaGuide/advanced/iteratees/Enumerators.md",
      "documentation/manual/scalaGuide/advanced/iteratees/Iteratees.md",
      "documentation/manual/scalaGuide/advanced/iteratees/_Sidebar.md",
      "documentation/manual/scalaGuide/advanced/routing/ScalaJavascriptRouting.md",
      "documentation/manual/scalaGuide/main/akka/ScalaAkka.md",
      "documentation/manual/scalaGuide/main/async/ScalaAsync.md",
      "documentation/manual/scalaGuide/main/async/ScalaComet.md",
      "documentation/manual/scalaGuide/main/async/ScalaStream.md",
      "documentation/manual/scalaGuide/main/async/ScalaWebSockets.md",
      "documentation/manual/scalaGuide/main/async/_Sidebar.md",
      "documentation/manual/scalaGuide/main/cache/ScalaCache.md",
      "documentation/manual/scalaGuide/main/forms/ScalaFormHelpers.md",
      "documentation/manual/scalaGuide/main/forms/ScalaForms.md",
      "documentation/manual/scalaGuide/main/forms/_Sidebar.md",
      "documentation/manual/scalaGuide/main/global/ScalaGlobal.md",
      "documentation/manual/scalaGuide/main/global/ScalaInterceptors.md",
      "documentation/manual/scalaGuide/main/global/_Sidebar.md",
      "documentation/manual/scalaGuide/main/http/ScalaActions.md",
      "documentation/manual/scalaGuide/main/http/ScalaActionsComposition.md",
      "documentation/manual/scalaGuide/main/http/ScalaBodyParsers.md",
      "documentation/manual/scalaGuide/main/http/ScalaContentNegotiation.md",
      "documentation/manual/scalaGuide/main/http/ScalaResults.md",
      "documentation/manual/scalaGuide/main/http/ScalaRouting.md",
      "documentation/manual/scalaGuide/main/http/ScalaSessionFlash.md",
      "documentation/manual/scalaGuide/main/http/_Sidebar.md",
      "documentation/manual/scalaGuide/main/http/images/routesError.png",
      "documentation/manual/scalaGuide/main/i18n/ScalaI18N.md",
      "documentation/manual/scalaGuide/main/json/ScalaJson.md",
      "documentation/manual/scalaGuide/main/json/ScalaJsonRequests.md",
      "documentation/manual/scalaGuide/main/json/_Sidebar.md",
      "documentation/manual/scalaGuide/main/sql/ScalaAnorm.md",
      "documentation/manual/scalaGuide/main/sql/ScalaDatabase.md",
      "documentation/manual/scalaGuide/main/sql/ScalaDatabaseOthers.md",
      "documentation/manual/scalaGuide/main/sql/_Sidebar.md",
      "documentation/manual/scalaGuide/main/sql/images/dbError.png",
      "documentation/manual/scalaGuide/main/templates/ScalaTemplateUseCases.md",
      "documentation/manual/scalaGuide/main/templates/ScalaTemplates.md",
      "documentation/manual/scalaGuide/main/templates/_Sidebar.md",
      "documentation/manual/scalaGuide/main/templates/images/templatesError.png",
      "documentation/manual/scalaGuide/main/tests/ScalaFunctionalTest.md",
      "documentation/manual/scalaGuide/main/tests/ScalaTest.md",
      "documentation/manual/scalaGuide/main/tests/_Sidebar.md",
      "documentation/manual/scalaGuide/main/upload/ScalaFileUpload.md",
      "documentation/manual/scalaGuide/main/ws/ScalaOAuth.md",
      "documentation/manual/scalaGuide/main/ws/ScalaOpenID.md",
      "documentation/manual/scalaGuide/main/ws/ScalaWS.md",
      "documentation/manual/scalaGuide/main/ws/_Sidebar.md",
      "documentation/manual/scalaGuide/main/xml/ScalaXmlRequests.md"
   ]
}
  """
}