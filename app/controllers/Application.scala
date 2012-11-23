package controllers

import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.ws.WS
import play.api.libs.concurrent.Execution.Implicits._
import markdown.PegdownConverter

object Application extends Controller {

  val MANUAL_BASE_URL = "https://raw.github.com/playframework/Play20/master/documentation/manual/"

  def index = Action {
    Redirect(routes.Application.render("Home"))
  }

  // https://github.com/playframework/Play20/tree-list/794a9e550745165c9e7f7573799e6b75c030161c

  def render(page: String) = Action { implicit request =>
    {
      Async {
        WS.url(MANUAL_BASE_URL + page + ".md").get().map { response =>
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