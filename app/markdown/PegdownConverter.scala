package markdown

import org.pegdown._
import org.pegdown.ast._
import controllers.routes

object PegdownConverter extends MarkdownConverter {

  def markdown2html(input: String, path: String): Option[String] = {
    input match {
      case null => None
      case _ => Some(pegdownConversion(input, path))
    }
  }

  private def pegdownConversion(input: String, path: String): String = {

    // TODO: it may be not thread safe ?
    new PegDownProcessor(Extensions.FENCED_CODE_BLOCKS + Extensions.WIKILINKS).markdownToHtml(input, new GithubLinkRenderer(path))

  }

}

class GithubLinkRenderer(val path: String) extends org.pegdown.LinkRenderer {

  lazy val conf = play.api.Play.current.configuration
  lazy val baseUrl = conf.getString("github.play2.baseUrl").get + path
  
  override def render(node: WikiLinkNode): LinkRenderer.Rendering = {

    val text = node.getText
    if (isImage(text)) {
      buildImage(text)
    } else {
      text.split("[|]") match {
        case Array(text, href) => buildLink(href.trim(), text)
        case _ => buildLink(text, text)
      }
    }
  }

  private def buildLink(href: String, text: String) = {

    if (href.startsWith("http")) {
      new LinkRenderer.Rendering(href, text.trim)
    } else {
      val link = routes.Application.render("master", href.trim).url
      new LinkRenderer.Rendering(link, text.trim)
    }
  }

  private def buildImage(href: String) = {
    val url = if (href.startsWith("http")) href
    else "%s/%s".format(baseUrl, href)
    new LinkRenderer.Rendering(url, """<img src="%s" alt="" />""".format(url)).withAttribute("target", "blank")
  }

  private def isImage(path: String): Boolean = {
    Seq("jpg", "jpeg", "png", "gif").exists(path.endsWith(_))
  }
}