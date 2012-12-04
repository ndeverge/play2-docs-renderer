package markdown

trait MarkdownConverter {

  def markdown2html(input: String): Option[String]

  def markdown2html(input: play.api.libs.ws.Response): Option[String] = {
    markdown2html(input.body)
  }

}