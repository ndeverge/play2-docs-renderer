package controllers

import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.ws.WS
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._
import markdown.PegdownConverter._
import github.GithubTree

object Application extends Controller {

  val MANUAL_BASE_URL = "https://raw.github.com/playframework/Play20/master/documentation/manual/"
  val EDIT_BASE_URL = "https://github.com/playframework/Play20/edit/master/documentation/manual"

  def index = Action {
    Redirect(routes.Application.render("Home"))
  }

  def render(page: String) = Action { implicit request =>
    Async {
      GithubTree.findPath(page).flatMap(pageFound =>
        pageFound match {
          case None => Future(NotFound("Not found on Github"))
          case Some(path) => showPage(path, page)
        }
      )
    }
  }

  private def showPage(path: String, pageName: String) : Future[Result] = {
    val sidebarFuture = WS.url(MANUAL_BASE_URL + path + "/_Sidebar.md").get()
    val pageFuture = WS.url(MANUAL_BASE_URL + path + "/" + pageName + ".md").get()
    val sections = for (sidebar <- sidebarFuture; page <- pageFuture) yield (sidebar, page)

    sections.map(_ match {
      case (sidebar, page) => {
        page.status match {
          case 200 => {
            val editLink = EDIT_BASE_URL + path + "/" + pageName + ".md"
            val html = markdown2html(page)
            val sidebarHtml = if (sidebar.status == 200) markdown2html(sidebar) else ""
            Ok(views.html.main(html, sidebarHtml, editLink))
          }
          case _ => Status(page.status)
        }
      }
      case _ => NotFound("Error during fetching page")
    })
  }

}