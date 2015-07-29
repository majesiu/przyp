package controllers

import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, Controller}
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import anorm._
import play.api.db.DB
import models.Event


class Application extends Controller {


  def index = Action {
    DB.withConnection { implicit c =>
      Ok(views.html.index(Event.findAll()))
    }
  }

  val addEventForm = Form(
    mapping(
      "name" -> text,
      "date" -> date,
      "creator" -> text
    )(Event.apply)(Event.unapply)
  )

  def add = Action {
    Ok(views.html.unit(addEventForm))
  }

  def addEvent = Action { implicit c =>
    addEventForm.bindFromRequest.fold(
    errors => BadRequest,
    {
      case Event(name, date, creator) =>
        Event.addEvent(Event(name, date, creator))
        Redirect("/new")
   }
    )
  }
}
