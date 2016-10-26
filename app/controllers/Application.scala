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
    Ok(toJson(games.list))
  }

  def insert = DBAction(parse.json) { implicit rs =>
    rs.request.body.validate[Game].map { game =>
      val dbGame = DbGame(game.player_1, game.player_2, game.score_1, game.score_1, Time.now)
      games.insert(dbGame)
      Ok(toJson(dbGame))
    }.getOrElse(BadRequest("invalid json"))
  }

}
