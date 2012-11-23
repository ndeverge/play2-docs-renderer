package markdown

import org.pegdown._
import org.pegdown.ast._
import controllers.routes

object PegdownConverter extends MarkdownConverter {

  def markdown2html(input: String): String = {
    input match {
      case null => null
      case _ => pegdownConversion(input)
    }
  }

  private def pegdownConversion(input: String): String = {

    // TODO: it may be not thread safe ?
    new PegDownProcessor(Extensions.AUTOLINKS + Extensions.WIKILINKS).markdownToHtml(input, new GithubLinkRenderer())

  }

}

class GithubLinkRenderer extends org.pegdown.LinkRenderer {

  override def render(node: WikiLinkNode): LinkRenderer.Rendering = {

    val text = node.getText
    text.split("[|]") match {
      case Array(text, href) => buildLink(href, text)
      case _ => buildLink("#", text + " (broken link)")
    }
  }

  private def buildLink(href: String, text: String) = {

    if (href.startsWith("http")) {
      new LinkRenderer.Rendering(href, text.trim)
    } else {
      val link = routes.Application.render(href.trim).url
      new LinkRenderer.Rendering(link, text.trim)
    }
  }
}