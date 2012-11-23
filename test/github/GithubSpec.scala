package github

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json._
import github.Github._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class GithubSpec extends Specification {

  "GithubSpec" should {

    "transform a json to a List" in {

      Github.json must not beNull

      val jsonTest = Json.parse(Github.json)
      Github.toList(jsonTest) must beAnInstanceOf[List[String]]

    }
    "clean the path to get only the directories at the root directory" in {

      val fullPath = "documentation/manual/Home.md"

      Github.cleanPath(List(fullPath), "Home") must beEqualTo(Some("/"))

    }

    "clean the path to get only the directories with a subdirectory" in {

      val fullPath = "documentation/manual/book/BookHome.md"

      Github.cleanPath(List(fullPath), "BookHome") must beEqualTo(Some("/book"))

    }

    "clean the path for the Home page" in {

      val fullPath = "documentation/manual/Home.md"

      Github.cleanPath(List(fullPath), "Home") must beEqualTo(Some("/"))

    }

    "find 'detailledTopics/assets/' when supplying 'AssetsLess'" in {

      Github.findPath("AssetsLess") must beEqualTo(Some("/detailledTopics/assets"))

    }

    "find '/' when supplying 'Home'" in {

      Github.findPath("Home") must beEqualTo(Some("/"))

    }

    "find '/javaGuide/' when supplying 'JavaHome'" in {

      Github.findPath("JavaHome") must beEqualTo(Some("/javaGuide"))

    }

  }
}