package models

import java.time._

import play.api.db.slick.Config.driver.simple._

case class Game(player_1: String, player_2: String, score_1: String, score_2: String)

case class DbGame(player_1: String, player_2: String, score_1: String, score_2: String, time_stamp: String)

object Time{
  val now = LocalDateTime.now().toString
}

/* Table mapping
 */
class GamesTable(tag: Tag) extends Table[DbGame](tag, "games") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def player_1 = column[String]("player_1", O.NotNull)

  def player_2 = column[String]("player_2", O.NotNull)

  def score_1 = column[String]("score_1", O.NotNull)

  def score_2 = column[String]("score_2", O.NotNull)

  def time_Stamp = column[String]("time_stamp", O.NotNull)

  def * = (player_1, player_2, score_1, score_2, time_Stamp) <> (DbGame.tupled, DbGame.unapply _)
}
