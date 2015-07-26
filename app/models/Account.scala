package models

import anorm.{ResultSetParser, ~, SqlParser, RowParser}

/**
 * Created by Ireneusz on 2015-07-26.
 */


sealed trait Role

case object Administrator extends Role

case object NormalUser extends Role

case class Account(name: String, pass: String, role: String) {

}

