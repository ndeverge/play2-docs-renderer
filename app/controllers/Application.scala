package controllers

import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.ws.WS
import scala.concurrent.{ Future, Promise }
import play.api.libs.concurrent.Execution.Implicits._
import markdown.PegdownConverter
import github.GithubTree

object Application extends Controller {

  val MANUAL_BASE_URL = "https://raw.github.com/playframework/Play20/master/documentation/manual/"

  def index = Action {
    Redirect(routes.Application.render("Home"))
  }

  def render(page: String) = Action { implicit request =>
    {
      GithubTree.findPath(page) match {
        case None => NotFound("Not found on Github")
        case Some(path) => {

          Async {

            val sideBar = WS.url(MANUAL_BASE_URL + path + "/_Sidebar.md").get()

            WS.url(MANUAL_BASE_URL + path + "/" + page + ".md").get().flatMap { response =>
              response.status match {
                case 200 => {
                  val html = PegdownConverter.markdown2html(response.body)

                  sideBar.map { sideBarResponse =>
                    sideBarResponse.status match {
                      case 200 => {
                        val sideBarHtml = PegdownConverter.markdown2html(sideBarResponse.body)
                        Ok(views.html.main(html, sideBarHtml))
                      }
                      case _ => Ok(views.html.main(html, ""))
                    }
                  }

                }
                case _ => Future(Status(response.status))
              }
            }
          }
        }
      }
    }
  }

}