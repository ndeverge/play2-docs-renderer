package controllers

import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.ws.WS
import play.api.libs.concurrent.Execution.Implicits._
import markdown.PegdownConverter
import github.Github

object Application extends Controller {

  val MANUAL_BASE_URL = "https://raw.github.com/playframework/Play20/master/documentation/manual/"

  def index = Action {
    Redirect(routes.Application.render("Home"))
  }

  def render(page: String) = Action { implicit request =>
    {
      println(page + " = " + Github.findPath(page))
      Github.findPath(page) match {
        case None => NotFound("Not found")
        case Some(path) => {

          Async {
            WS.url(MANUAL_BASE_URL + path + page + ".md").get().map { response =>
              response.status match {
                case 200 => {
                  val html = PegdownConverter.markdown2html(response.body)
                  Ok(views.html.markdown(html))
                }
                case _ => Status(response.status)
              }

            }
          }
        }

      }
    }
  }

}