package markdown

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class MarkdownConverterSpec extends Specification {

  private def doConvert(text: String) = {
    PegdownConverter.markdown2html(text)
  }

  "MarkdownConverter" should {

    "fail on null string" in {

      PegdownConverter.markdown2html(null) must beNull
    }

    "return a HTML paragraph given a simple string" in {

      val text = "A simple string"

      doConvert(text) must beEqualTo("<p>" + text + "</p>")
    }

    "return a bold HTML string given a bold markdown string" in {

      val text = "A bold string"
      val boldText = "**" + text + "**"

      doConvert(boldText) must beEqualTo("<p><strong>" + text + "</strong></p>")
    }

  }
}