package controllers

import models.{SearchModel}
import play.api.Play.current
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, Controller}
import play.api.i18n.Messages.Implicits._
import anorm._
import play.api.db.DB
import views.html.helper.select

/**
 * Created by user on 2015-07-28.
 */
class Search extends Controller{

  val addSearchForm = Form(
    mapping(
      "name" -> optional(text),
      "nameOp" -> text,
      "date" -> optional(date),
      "email" -> optional(text)
    )(SearchModel.apply)(SearchModel.unapply)
  )

  def search = Action {
    Ok(views.html.search(addSearchForm))
  }

  def startSearch = Action { implicit c =>
    addSearchForm.bindFromRequest.fold(
    errors => BadRequest,
    {
      case SearchModel(name,nameOp, date, creator) =>
        DB.withConnection { implicit c =>
          Ok(views.html.index(SearchModel.findFilter(new SearchModel(name,nameOp,date,creator))))
        }
    }
    )
  }
}
