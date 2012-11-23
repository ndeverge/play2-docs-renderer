package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class ApplicationSpec extends Specification {

  "Application" should {

    "send 404 on a bad request" in {
      running(FakeApplication()) {
        route(FakeRequest(GET, "/boum")) must beNone
      }
    }

    "redirect on the index page" in {
      running(FakeApplication()) {
        val home = route(FakeRequest(GET, "/")).get

        status(home) must equalTo(303)
      }
    }

    "render a markdown html" in {

      running(FakeApplication()) {
        val markdownRenderer = route(FakeRequest(GET, "/render/Home")).get

        status(markdownRenderer) must equalTo(OK)
        contentType(markdownRenderer) must beSome.which(_ == "text/html")
        contentAsString(markdownRenderer) must contain("Play 2.1 documentation")
      }

    }
  }
}