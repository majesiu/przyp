package controllers

import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, Controller}
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import anorm._
import play.api.db.DB
import models.People


class Application extends Controller {

  val myCaseClassParser: RowParser[People] = (
      SqlParser.int("id") ~
      SqlParser.str("name")~
      SqlParser.int("age")
    ) map {
    case cval1 ~ cval2 ~ cval3 =>
      new People(cval1, cval2, cval3)
  }

  val allRowsParser: ResultSetParser[List[People]] = myCaseClassParser.*

  def index = Action {
    DB.withConnection { implicit c =>
      Ok(views.html.index(SQL("Select * from people").as(allRowsParser).toSeq))
    }
  }

  val addUnitForm = Form(
    mapping(
      "id" -> number,
      "name" -> nonEmptyText,
      "age" -> number
     )(People.apply)(People.unapply)
  )

  def add = Action {
    Ok(views.html.unit(addUnitForm))
  }

  def addPeople = Action { implicit c =>
    addUnitForm.bindFromRequest.fold(
    errors => BadRequest,
    {
      case People(id,name,age) =>
        People.addPeople(People(id,name,age))
        Ok("Book Successfully added!")
    }
    )
  }
}
