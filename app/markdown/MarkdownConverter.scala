package markdown

trait MarkdownConverter {

  def markdown2html(input: String): String

}