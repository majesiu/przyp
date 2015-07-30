package controllers

import java.io.File
import javax.inject.Inject

import models.{Reminder, Account, Event}
import org.apache.commons.mail.EmailAttachment
import play.api.Configuration
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.mailer._
import play.api.mvc.{Action, Controller}
import play.api.Play.current
import play.api.i18n.Messages.Implicits._

class Mailing @Inject()(mailer: MailerClient) extends Controller {

  def newMail = Action {
    Ok(views.html.mail(addMailForm))
  }

  val addMailForm = Form(
    mapping(
      "name" -> text,
      "date" -> date,
      "creator" -> text,
      "email" -> email
    )(Reminder.apply)(Reminder.unapply)
  )

  def mailReminder() = Action { implicit c =>
    addMailForm.bindFromRequest.fold(
    errors => BadRequest,
    {
      case Reminder(name, date, creator, emailn) =>
        val email = Email(
          "Przypomnienie o wydarzeniu",
          "Usluga mailingowa <sutemcio@gmail.com>",
          Seq(emailn),
          bodyText = Some("Przypomnienie o wydarzeniu: " + name + " dnia " + date + " utworzynym przez: "
            + creator)
        )
        mailer.send(email)
        Redirect("/index")
    }
    )
  }

  /*
  def mailTest = Action {
    val email = Email(
      "Simple email",
      "Mister FROM <from@email.com>",
      Seq("Miss TO <sutemcio@gmail.com>"),
      attachments = Seq(
        AttachmentFile("favicon.png", new File(current.classloader.getResource("public/images/favicon.png").getPath)),
        AttachmentData("data.txt", "data".getBytes, "text/plain", Some("Simple data"), Some(EmailAttachment.INLINE))
      ),
      bodyText = Some("A text message"),
      bodyHtml = Some("<html><body><p>An <b>html</b> message</p></body></html>")
    )
    val id = mailer.send(email)
    Ok(s"Email $id sent!")
  }
  /*
  def configureAndSend = Action {
    val email = Email("Simple email", "from@email.com", Seq("to@email.com"))
    val id = mailer.configure(Configuration.from(Map("host" -> "typesafe.org", "port" -> 1234))).send(email)
    Ok(s"Email $id sent!")
  }*/*/
}