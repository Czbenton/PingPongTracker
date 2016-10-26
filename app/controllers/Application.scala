package controllers

import models.{GamesTable, _}
import play.api.Play.current
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick._
import play.api.libs.json.Json
import play.api.libs.json.Json._
import play.api.mvc._

object Application extends Controller {

  //create an instance of the table
  val games = TableQuery[GamesTable]

  //JSON read/write macro
  implicit val gameFormat = Json.format[Game]
  implicit val dbGameFormat = Json.format[DbGame]

  def convertGame(game : DbGame)  = {
    Json.obj(
      "id" -> game.id,
      "players" -> List(
        Json.obj("name" -> game.player_1, "score" -> game.score_1),
        Json.obj("name" -> game.player_2, "score" -> game.score_2)
      ),
      "time" -> game.time_stamp
    )
  }


  def jsonFindAll = DBAction { implicit rs =>
    val allGames = games.list.map(convertGame)

    Ok(Json.obj("games" -> allGames)).withHeaders(
      "Access-Control-Allow-Origin" -> "*",
      "Access-Control-Allow-Methods" -> "GET, POST, PUT, DELETE, OPTIONS",
      "Access-Control-Allow-Headers" -> "Accept, Origin, Content-type, X-Json, X-Prototype-Version, X-Requested-With",
      "Access-Control-Allow-Credentials" -> "true",
      "Access-Control-Max-Age" -> (60 * 60 * 24).toString
    )
  }

  def insert = DBAction(parse.json) { implicit rs =>
    rs.request.body.validate[Game].map { game =>
      val dbGame = DbGame(0, game.player_1, game.player_2, game.score_1, game.score_1, Time.now)
      games.insert(dbGame)

      Ok(Json.obj("game" -> convertGame(dbGame))).withHeaders(
        "Access-Control-Allow-Origin" -> "*",
        "Access-Control-Allow-Methods" -> "GET, POST, PUT, DELETE, OPTIONS",
        "Access-Control-Allow-Headers" -> "Accept, Origin, Content-type, X-Json, X-Prototype-Version, X-Requested-With",
        "Access-Control-Allow-Credentials" -> "true",
        "Access-Control-Max-Age" -> (60 * 60 * 24).toString
      )
    }.getOrElse(BadRequest("invalid json"))
  }

  def preflight(all: String) = {
    Ok(Json.obj("status" -> "ok")).withHeaders(
      "Access-Control-Allow-Origin" -> "*",
      "Access-Control-Allow-Methods" -> "GET, POST, PUT, DELETE, OPTIONS",
      "Access-Control-Allow-Headers" -> "Accept, Origin, Content-type, X-Json, X-Prototype-Version, X-Requested-With",
      "Access-Control-Allow-Credentials" -> "true",
      "Access-Control-Max-Age" -> (60 * 60 * 24).toString
    )
  }
}
