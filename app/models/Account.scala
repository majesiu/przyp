package models

import anorm._
import play.api.db.DB
import play.api.Play.current

/**
 * Created by Ireneusz on 2015-07-26.
 */


sealed trait Role

case object Administrator extends Role

case object NormalUser extends Role

case class Account(name: String, pass: String, role: String) {

}

object Account {
  val simple = {
      SqlParser.str("name") ~
      SqlParser.str("pass") ~
      SqlParser.str("role") map {
      case name ~ pass ~ role => Account(name, pass, role)
    }
  }

  def unapply2(account: Account): Option[(String, String)] = {
    if (account.role == "user"){
      Some(account.name, account.pass)
    }else{
      None
    }
  }

  //TO DO: unique username in DB (maybe as a key?) or double name for admin purposes
  def findAccount(name: String): Seq[Account] = {
    DB.withConnection { implicit connection =>
      SQL("select * from accounts where name = {name}")
        .on('name -> name)
        .as(Account.simple *)
    }
  }

  def verify(account: Account): Boolean ={
    val check = Account.findAccount(account.name)
    if(check.isEmpty) false
    else if(check.head.pass == account.pass)  true
    else  false
  }

  def addAccount(account: Account): Unit = {
    DB.withConnection { implicit c =>
      SQL("insert into accounts(name, pass, role) values ({name}, {pass}, {role})").on(
        'name -> account.name,
        'pass -> account.pass,
        'role -> account.role
      ).executeUpdate()
    }
  }
}
