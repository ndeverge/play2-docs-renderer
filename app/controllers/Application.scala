package controllers

import play.api._
import play.api.mvc._
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._
import markdown.PegdownConverter._
import github._

object Application extends Controller {

  lazy val conf = play.api.Play.current.configuration

  def index = Action {
    Redirect(routes.Application.render("master", "Home"))
  }

  def render(branch: String, page: String) = Action {
    Async {
      GithubTree.findPath(branch, page).flatMap(pageFound =>
        pageFound match {
          case None => Future(NotFound("Not found on Github"))
          case Some(path) => showPage(branch, path, page)
        }
      )
    }
  }

  private def showPage(branch: String, path: String, pageName: String) : Future[Result] = {
    val sidebarFuture = GithubPage.fetchPageFromCache(branch, path + "/_Sidebar.md")
    val pageFuture = GithubPage.fetchPageFromCache(branch, path + "/" + pageName + ".md")
    val sections = for (sidebar <- sidebarFuture; page <- pageFuture) yield (sidebar, page)

    sections.map(_ match {
      case (sidebar, page) => {
        page.status match {
          case 200 => {
            val editLink = conf.getString("github.play2.editUrl").get + path + "/" + pageName + ".md"
            val html = markdown2html(page.body, branch, path).getOrElse("")
            val sidebarHtml = if (sidebar.status == 200) markdown2html(sidebar.body, branch, path).getOrElse("") else ""
            Ok(views.html.main(html, sidebarHtml, editLink, branch))
          }
          case _ => Status(page.status)
        }
      }
      case _ => NotFound("Error during fetching page")
    })
  }

}