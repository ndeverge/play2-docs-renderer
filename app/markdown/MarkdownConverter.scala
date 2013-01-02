package markdown

trait MarkdownConverter {

  def markdown2html(input: String, branch: String, path: String): Option[String]

}