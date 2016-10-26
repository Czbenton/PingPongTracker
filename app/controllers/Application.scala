package controllers

import models._
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


  def jsonFindAll = DBAction { implicit rs =>
    Ok(Json.obj("games" -> toJson(games.list))).withHeaders(
      "Access-Control-Allow-Origin" -> "*",
      "Access-Control-Allow-Methods" -> "GET, POST, PUT, DELETE, OPTIONS",
      "Access-Control-Allow-Headers" -> "Accept, Origin, Content-type, X-Json, X-Prototype-Version, X-Requested-With",
      "Access-Control-Allow-Credentials" -> "true",
      "Access-Control-Max-Age" -> (60 * 60 * 24).toString
    )
  }

  def insert = DBAction(parse.json) { implicit rs =>
    rs.request.body.validate[Game].map { game =>
      val dbGame = DbGame(game.player_1, game.player_2, game.score_1, game.score_1, Time.now)
      games.insert(dbGame)
      Ok(Json.obj("game" -> toJson(dbGame))).withHeaders(
        "Access-Control-Allow-Origin" -> "*",
        "Access-Control-Allow-Methods" -> "GET, POST, PUT, DELETE, OPTIONS",
        "Access-Control-Allow-Headers" -> "Accept, Origin, Content-type, X-Json, X-Prototype-Version, X-Requested-With",
        "Access-Control-Allow-Credentials" -> "true",
        "Access-Control-Max-Age" -> (60 * 60 * 24).toString
      )
    }.getOrElse(BadRequest("invalid json"))
  }

}
