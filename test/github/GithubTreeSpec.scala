package github

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json._
import github.GithubTree._
import org.specs2.matcher._
import scala.concurrent.{Await, Awaitable}
import scala.concurrent.duration.Duration


/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class GithubTreeSpec extends Specification {

  "GithubTreeSpec" should {

    "Fetch json from github to page list" in new WithApplication {
      val pages = await(GithubTree.getDocumentationsPages("master"))
      pages must beAnInstanceOf[List[String]]
      pages.size must beGreaterThan(0)
    }

    "clean the path to get only the directories at the root directory" in {
      val fullPath = "documentation/manual/Home.md"
      GithubTree.cleanPath(List(fullPath), "Home") must beSome("")
    }

    "clean the path to get only the directories with a subdirectory" in {
      val fullPath = "documentation/manual/book/BookHome.md"
      GithubTree.cleanPath(List(fullPath), "BookHome") must beSome("/book")
    }

    "clean the path for a page containing numbers" in {
      val fullPath = "documentation/manual/scalaGuide/main/i18n/ScalaI18N.md"
      GithubTree.cleanPath(List(fullPath), "ScalaI18N") must beSome("/scalaGuide/main/i18n")
    }

    "clean the path for the Home page" in {
      val fullPath = "documentation/manual/Home.md"
      GithubTree.cleanPath(List(fullPath), "Home") must beEqualTo(Some(""))
    }

    "find 'detailledTopics/assets/' when supplying 'AssetsLess'" in new WithApplication {
      await(GithubTree.findPath("master", "AssetsLess")) must beSome("/detailledTopics/assets")
    }

    "find '/' when supplying 'Home'" in new WithApplication {
      await(GithubTree.findPath("master", "Home")) must beEqualTo(Some(""))
    }

    "find '/javaGuide/' when supplying 'JavaHome'" in new WithApplication {
      await(GithubTree.findPath("master", "JavaHome")) must beSome("/javaGuide")
    }
    
    "find '/build/' when supplying 'Build'" in new WithApplication {
      await(GithubTree.findPath("master", "Build")) must beSome("/build")
    }.pendingUntilFixed("Issue #12")
    
    "find the sha by filtering the json using the branch name" in {
      val expected = Json.parse(branchesAsJson).asInstanceOf[JsArray](2)
      GithubTree.filterByName(Json.parse(branchesAsJson), "master") must beSome(expected)
    }
    
    "find the sha by filtering the json using the tag name" in {
      val expected = Json.parse(tagsAsJson).asInstanceOf[JsArray](2)
      GithubTree.filterByName(Json.parse(tagsAsJson), "2.1-RC1") must beSome(expected)
    }
    
  }

  def await[T](future: Awaitable[T]) : T = {
    Await.result(future, Duration(5, "seconds"))
  }
  
  val branchesAsJson = """
[
  {
    "commit": {
      "url": "https://api.github.com/repos/playframework/Play20/commits/70f69bc007178b04b383dc75a9b492dc8d69a0f2",
      "sha": "70f69bc007178b04b383dc75a9b492dc8d69a0f2"
    },
    "name": "2.0.x"
  },
  {
    "commit": {
      "url": "https://api.github.com/repos/playframework/Play20/commits/a61a48e35a4c6c0e0d28966c84bcbbd0d4d4014a",
      "sha": "a61a48e35a4c6c0e0d28966c84bcbbd0d4d4014a"
    },
    "name": "jerkson-free"
  },
  {
    "commit": {
      "url": "https://api.github.com/repos/playframework/Play20/commits/9e967d77bd381217ceb85d5fc01a0dbc2ad444d1",
      "sha": "9e967d77bd381217ceb85d5fc01a0dbc2ad444d1"
    },
    "name": "master"
  },
  {
    "commit": {
      "url": "https://api.github.com/repos/playframework/Play20/commits/8b242b1e79f9d09970b2ab066e914f32601605a1",
      "sha": "8b242b1e79f9d09970b2ab066e914f32601605a1"
    },
    "name": "cleaning-execution-context"
  },
  {
    "commit": {
      "url": "https://api.github.com/repos/playframework/Play20/commits/6c421f7c5d46aafc68add1dd147c468cb86179ee",
      "sha": "6c421f7c5d46aafc68add1dd147c468cb86179ee"
    },
    "name": "scala-futures"
  }
]
    
    """
    
    val tagsAsJson = """
    [
  {
    "zipball_url": "https://github.com/playframework/Play20/archive/2.0-RC5.zip",
    "commit": {
      "sha": "5698130921a6129b4ca24c02c1e2645d2e1e97e4",
      "url": "https://api.github.com/repos/playframework/Play20/commits/5698130921a6129b4ca24c02c1e2645d2e1e97e4"
    },
    "name": "2.0-RC5",
    "tarball_url": "https://github.com/playframework/Play20/archive/2.0-RC5.tar.gz"
  },
  {
    "zipball_url": "https://github.com/playframework/Play20/archive/2.0-RC1.zip",
    "commit": {
      "sha": "93f0169f10b6fe0788a565d85692857866eb68b1",
      "url": "https://api.github.com/repos/playframework/Play20/commits/93f0169f10b6fe0788a565d85692857866eb68b1"
    },
    "name": "2.0-RC1",
    "tarball_url": "https://github.com/playframework/Play20/archive/2.0-RC1.tar.gz"
  },
  {
    "zipball_url": "https://github.com/playframework/Play20/archive/2.1-RC1.zip",
    "commit": {
      "sha": "ba571ef1c99ec94c94d9eab9986a4c5c6dfa3b3e",
      "url": "https://api.github.com/repos/playframework/Play20/commits/ba571ef1c99ec94c94d9eab9986a4c5c6dfa3b3e"
    },
    "name": "2.1-RC1",
    "tarball_url": "https://github.com/playframework/Play20/archive/2.1-RC1.tar.gz"
  }
]  
    """
}