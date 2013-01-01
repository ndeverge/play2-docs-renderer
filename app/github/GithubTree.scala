package github

import scala.Option.option2Iterable
import scala.Some.apply
import scala.concurrent.Future

import play.api.Play.current
import play.api.cache.Cache
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.JsArray
import play.api.libs.json.JsValue
import play.api.libs.ws.WS

object GithubTree {

  private lazy val conf = current.configuration
  private lazy val cacheDuration = conf.getInt("github.play2.cache").getOrElse(600)

  def findPath(branch: String, link: String): Future[Option[String]] = {
    getDocumentationsPagesFromCache(branch).map{files =>
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

  def getDocumentationsPages(branch: String): Future[List[String]] = {
    
    val treeFuture =
      for (shaFromBranch <- fetchLastCommitShaForBranch(branch);
    	   shaFromTag <- fetchLastCommitShaForTag(branch);
           githubTree <- fetchGithubTree(Seq(shaFromBranch, shaFromTag)))
      yield (githubTree)

    treeFuture.map{ tree =>
      val filepaths = (tree \ "paths").as[List[String]]
      filepaths.filter(path => path.endsWith(".md"))
    }
  }

  def getDocumentationsPagesFromCache(branch: String): Future[List[String]] = {
    Cache.getOrElse("github.play2.treelist." + branch, cacheDuration)(getDocumentationsPages(branch))
  }

  private def fetchGithubTree(shas: Seq[Option[String]]): Future[JsValue] = {
    val shasFound = shas.flatten
    
    val sha = shasFound(0) // take the first hoping there's one...
    
    WS.url(conf.getString("github.play2.dir").get.format(sha)).get.map(_.json)
  }

  
  private def fetchLastCommitShaForTag(branch: String) : Future[Option[String]] = {
    fetchLastCommitSha("github.play2.tags", branch)
  }
  
  private def fetchLastCommitShaForBranch(branch: String) : Future[Option[String]] = {
    fetchLastCommitSha("github.play2.branches", branch)
  }
  
  private def fetchLastCommitSha(githubCall: String, branch: String) : Future[Option[String]] = {
    WS.url(conf.getString(githubCall).get)
      .get.map(r => 
        filterByName(r.json, branch) match {
          case Some(e) => Some((e \ "commit" \ "sha").as[String])
          case None => None
        } 
      )
  }
  
  def filterByName(json: JsValue, branch: String) = {
   json match {
     case array: JsArray => {
       val filteredByName = array.value.filter(p => ((p \ "name").toString.equals("\"" + branch + "\"")))
       filteredByName match {
	     case Seq() => None
	     case head :: tail => Some(head)
	   }
     }
     case _ => None
   }
   
   
  }

}