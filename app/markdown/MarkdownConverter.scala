package markdown

trait MarkdownConverter {

  def markdown2html(input: String, path: String): Option[String]

}