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
    PegdownConverter.markdown2html(text, "PATH")
  }

  "MarkdownConverter" should {

    "fail on null string" in {

      doConvert(null: String) must beNone
    }

    "return a HTML paragraph given a simple string" in {

      val text = "A simple string"

      doConvert(text) must beSome("<p>" + text + "</p>")
    }

    "return a bold HTML string given a bold markdown string" in {

      val text = "A bold string"
      val boldText = "**" + text + "**"

      doConvert(boldText) must beSome("<p><strong>" + text + "</strong></p>")
    }
    
    "return an image with a blank link for image tag" in new WithApplication {
      val text = "[[images/hello.png]]"
      val result = doConvert(text)
      result must beSome
      result.get must contain("""<img src="https://raw.github.com/playframework/Play20/%s/documentation/manual/PATH/images/hello.png" alt="" />""")
    }
    
    "return a correct Url when providing an Url with spaces" in {
      val text = "[[nginx| http://wiki.nginx.org/Main]]"
      val result = doConvert(text)
      result must beSome
      result must beSome("""<p><a href="http://wiki.nginx.org/Main">nginx</a></p>""")
    }
    
    "return a correct Url when providing just the Url without name" in {
      val text = "[[https://play.lighthouseapp.com/projects/82401-play-20/overview]]"
      val result = doConvert(text)
      result must beSome
      result must beSome("""<p><a href="https://play.lighthouseapp.com/projects/82401-play-20/overview">https://play.lighthouseapp.com/projects/82401-play-20/overview</a></p>""")
    }

  }
}