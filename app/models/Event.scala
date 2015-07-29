package models

import java.util.Date

import anorm._
import play.api.db.DB
import play.api.Play.current

/**
 * Created by Ireneusz on 2015-07-26.
 */
case class Event(name: String, date: Date, creator: String) {
  def toHtml = name + "\t" + date + "\t" + creator
}

object Event {
  val simple = {
      SqlParser.str("name") ~
      SqlParser.date("date") ~
      SqlParser.str("creator") map {
      case name ~ date ~ creator => Event(name, date, creator)
    }
  }

  def findAll(): Seq[Event] = {
    DB.withConnection { implicit connection =>
      SQL("select * from Events").as(Event.simple *)
    }
  }

  def addEvent(event: Event): Unit = {
    DB.withConnection { implicit c =>
      SQL("insert into Events(name, date, creator) values ({name}, {date}, {creator})").on(
        'name -> event.name,
        'date -> event.date,
        'creator -> event.creator
      ).executeUpdate()
    }
  }
}
