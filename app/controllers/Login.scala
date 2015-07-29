package controllers

import anorm._
import models.{Event, Account}
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.DB
import play.api.mvc.{Action, Controller}
import play.api.Play.current
import play.api.i18n.Messages.Implicits._


class Login extends Controller {

  def login = Action {
    Ok(views.html.login(addLoginForm))
  }

  val addLoginForm = Form(
    mapping(
      "name" -> text,
      "pass" -> text
    )((name, pass) => Account(name, pass, "normal"))((account: Account) => Some(account.name, account.pass))
  )

  def verify = Action { implicit c =>
    addLoginForm.bindFromRequest.fold(
    errors => Ok("Bledny formularz"),
    {
      case Account(name, pass, role) =>
        if(Account.verify(Account(name, pass, role))) Redirect("/index")
        else Ok("Logowanie nieudane")
    }
    )
  }


  def register = Action {
    Ok(views.html.register(addAccountForm))
  }

  val addAccountForm = Form(
    mapping(
      "name" -> text,
      "pass" -> text,
      "accept" -> checked("Zaakceptuj warunki")
    )((name, pass, _) => Account(name, pass, "normal"))((account: Account) => Some(account.name, account.pass, false))
  )

  def addAccount = Action { implicit c =>
    addAccountForm.bindFromRequest.fold(
    errors => Ok("Bledny formularz"),
    {
      case Account(name, pass, role) =>
        Account.addAccount(Account(name,pass,role))
        Redirect("/")
    }
    )
  }



}
