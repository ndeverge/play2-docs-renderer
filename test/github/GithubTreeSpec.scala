package github

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json._
import github.GithubTree._
import org.specs2.matcher._

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

      GithubTree.cleanPath(List(fullPath), "Home") must beEqualTo(Some(""))

    }

    "clean the path to get only the directories with a subdirectory" in {

      val fullPath = "documentation/manual/book/BookHome.md"

      GithubTree.cleanPath(List(fullPath), "BookHome") must beEqualTo(Some("/book"))

    }

    "clean the path for a page containing numbers" in {

      val fullPath = "documentation/manual/scalaGuide/main/i18n/ScalaI18N.md"

      GithubTree.cleanPath(List(fullPath), "ScalaI18N") must beEqualTo(Some("/scalaGuide/main/i18n"))

    }

    "clean the path for the Home page" in {

      val fullPath = "documentation/manual/Home.md"

      GithubTree.cleanPath(List(fullPath), "Home") must beEqualTo(Some(""))

    }

    "find 'detailledTopics/assets/' when supplying 'AssetsLess'" in {

      GithubTree.findPath("AssetsLess") must beEqualTo(Some("/detailledTopics/assets"))

    }

    "find '/' when supplying 'Home'" in {

      GithubTree.findPath("Home") must beEqualTo(Some(""))

    }

    "find '/javaGuide/' when supplying 'JavaHome'" in {

      GithubTree.findPath("JavaHome") must beEqualTo(Some("/javaGuide"))

    }
    
    lazy val sourceTree = io.Source.fromInputStream(getClass.getResourceAsStream("/githubSourceTree.json")).mkString
    
    "the json resource for test should be readable" in {
      sourceTree must contain("sha")
    }
    
    "parse the string as a Json object without error" in {
      GithubTree.parse(sourceTree) must beAnInstanceOf[Some[GithubTree]]
    }

    "allow to navigate through the Github tree" in {
      GithubTree.parse(sourceTree) match {
        case Some(tree) => tree.sha must beEqualTo("46d04362930b6fbde655aac14c1f24881c6cd451")
        case None => failure("Unable to parse the Json")
      }
    }

  }
}