package github

import play.api.libs.json._
import scala.util.matching.Regex
import play.api.libs.functional.syntax._
import play.api.libs.ws.WS
import play.api.Play
import play.api.libs.concurrent.Execution.Implicits._
import scala.concurrent.Future

object GithubTree {

  private lazy val conf = Play.current.configuration

  def findPath(link: String): Future[Option[String]] = {
    getDocumentationsPages().map{files =>
      val matchFiles = files.filter(_.contains(link))
      cleanPath(matchFiles, link)
    }
  }

  def cleanPath(pathsToClean: List[String], link: String): Option[String] = {
    if (pathsToClean.contains("documentation/manual/" + link + ".md")) {
      Some("")
    } else {
      def pattern = ("documentation/manual([A-Za-z0-9/]+)/" + link + ".md").r
      val found = pathsToClean.flatMap(pathToClean => pattern.findFirstMatchIn(pathToClean) match {
        case Some(matching) => Some(matching.group(1))
        case None => None
      })
      if (found.size == 1)
        Some(found(0))
      else None
    }
  }

  def getDocumentationsPages(): Future[List[String]] = {
    val treeFuture =
      for (sha <- fetchLastCommitSha();
           githubTree <- fetchGithubTree(sha))
      yield (githubTree)

    treeFuture.map{ tree =>
      val filepaths = (tree \ "paths").as[List[String]]
      filepaths.filter(path => path.endsWith(".md"))
    }
  }

  private def fetchGithubTree(sha: String): Future[JsValue] = {
    WS.url(conf.getString("github.play2.dir").get.format(sha))
      .get.map(_.json)
  }

  private def fetchLastCommitSha() : Future[String] = {
    WS.url(conf.getString("github.play2.lastCommit").get)
    .get.map(r => (r.json(0) \ "commit" \ "tree" \ "sha").as[String])
  }

}