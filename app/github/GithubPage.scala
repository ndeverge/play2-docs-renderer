package github

import play.api.libs.ws.WS
import play.api.cache.Cache
import play.api.Play.current
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._

object GithubPage {

  private lazy val conf = current.configuration
  private lazy val cacheDuration = conf.getInt("github.play2.cache").getOrElse(600)

  def fetchPageFromCache(url: String): Future[CachedPage] = {
    Cache.getOrElse(url, cacheDuration)(fetchPage(url))
  }

  def fetchPage(url: String) : Future[CachedPage] = {
    WS.url(conf.getString("github.play2.baseUrl").get.format("master") + url).get.map(response => 
        CachedPage(response.status, response.body))
  }

  sealed case class CachedPage(status: Int, body: String)

}