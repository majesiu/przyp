package models

import anorm.SqlParser._

import anorm._
import play.api.db.DB
import play.api.Play.current

case class People(id: Int, name: String, age: Int) {
  def toHtml = id + "\t" + name + "\t" + age
}

  object People{


    val simple = {
        get[Int]("unit.id") ~
        get[String]("unit.name") ~
        get[Int]("unit.age") map {
        case id~name~age => People(id,name,age)
      }
    }

    def findAll(): Seq[People] = {
      DB.withConnection { implicit connection =>
        SQL("select * from buch").as(People.simple *)
      }
    }

    def addPeople(people: People): Unit = {
      DB.withConnection { implicit c =>
        SQL("insert into people(name, age) values ({name}, {age})").on(
          'name -> people.name,
          'age -> people.age
        ).executeUpdate()
      }
    }
  }
