package controllers

import anorm._
import models.Account
import play.api.db.DB
import play.api.mvc.{Action, Controller}
import play.api.Play.current

class Login extends Controller {

  def login = Action {
    Ok(views.html.login("Login"))
  }


  val accountParser: RowParser[Account] = (
    SqlParser.str("name") ~
      SqlParser.str("pass") ~
      SqlParser.str("role")
    ) map {
    case cval1 ~ cval2 ~ cval3 =>
      new Account(cval1, cval2, cval3)
  }

  val allRowsParser: ResultSetParser[List[Account]] = accountParser.*
}
