package models

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

import anorm._
import play.api.db.DB
import play.api.Play.current

/**
 * Created by user on 2015-07-28.
 */
case class SearchModel (name: Option[String],nameOp: String, date: Option[Date],dateOp: String, creator: Option[String], creaOp: String) {
  def toHtml = name + "\t" + date + "\t" + creator
}



object SearchModel{
  val yearFormat = new SimpleDateFormat("yyyy-mm-dd")

  def findFilter(search: SearchModel): Seq[Event] = {
    DB.withConnection { implicit connection =>
      SQL("select * from Events " +
        "where name " + search.nameOp + " {name} and date " + search.dateOp + " {date} and creator "+ search.creaOp +" {creator}").on(
          'name -> search.name.getOrElse("%"),
          'date -> search.date.getOrElse(Calendar.getInstance.getTime),
          'creator -> search.creator.getOrElse("%")
        ).as(Event.simple *)
    }
  }
}