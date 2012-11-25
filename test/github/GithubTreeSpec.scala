package github

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json._
import github.GithubTree._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class GithubTreeSpec extends Specification {

  "GithubTreeSpec" should {

    "transform a json to a List" in {

      val jsonTest = Json.parse(GithubTree.fetchFileTreeAsJson)
      GithubTree.toList(jsonTest) must beAnInstanceOf[List[String]]

    }
    "clean the path to get only the directories at the root directory" in {

      val fullPath = "documentation/manual/Home.md"

      GithubTree.cleanPath(List(fullPath), "Home") must beEqualTo(Some("/"))

    }

    "clean the path to get only the directories with a subdirectory" in {

      val fullPath = "documentation/manual/book/BookHome.md"

      GithubTree.cleanPath(List(fullPath), "BookHome") must beEqualTo(Some("/book"))

    }

    "clean the path for the Home page" in {

      val fullPath = "documentation/manual/Home.md"

      GithubTree.cleanPath(List(fullPath), "Home") must beEqualTo(Some("/"))

    }

    "find 'detailledTopics/assets/' when supplying 'AssetsLess'" in {

      GithubTree.findPath("AssetsLess") must beEqualTo(Some("/detailledTopics/assets"))

    }

    "find '/' when supplying 'Home'" in {

      GithubTree.findPath("Home") must beEqualTo(Some("/"))

    }

    "find '/javaGuide/' when supplying 'JavaHome'" in {

      GithubTree.findPath("JavaHome") must beEqualTo(Some("/javaGuide"))

    }

  }
}