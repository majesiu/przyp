package models

import java.util.Date

import anorm._
import play.api.db.DB
import play.api.Play.current

/**
 * Created by user on 2015-07-28.
 */
case class SearchModel (name: Option[String], date: Option[Date], creator: Option[String]) {
  def toHtml = name + "\t" + date + "\t" + creator
}

object SearchModel{

  def findFilter(search: SearchModel): Seq[Event] = {
    DB.withConnection { implicit connection =>
      SQL("select * from Events " +
        "where name like {name}").on(
          'name -> search.name
        ).as(Event.simple *)
    }
  }

}