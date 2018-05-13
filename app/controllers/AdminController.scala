package controllers

import java.net.URLDecoder
import java.util.UUID

import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import javax.inject.Inject
import models.services.{ AuthTokenService, UserService }
import play.api.i18n.{ I18nSupport, Messages }
import play.api.libs.mailer.{ Email, MailerClient }
import play.api.mvc.{ AbstractController, AnyContent, ControllerComponents, Request }
import utils.auth.DefaultEnv
import be.objectify.deadbolt.scala.{ AuthenticatedRequest, DeadboltActions }
import play.api.libs.json.Json

import scala.concurrent.{ ExecutionContext, Future }

/**
 * The `Admin` controller.
 *
 * @param components       The Play controller components.
 * @param silhouette       The Silhouette stack.
 * @param userService      The user service implementation.
 * @param deadbolt         The deadbolt permissions.
 * @param ex               The execution context.
 */
class AdminController @Inject() (
  components: ControllerComponents,
  silhouette: Silhouette[DefaultEnv],
  userService: UserService,
  deadbolt: DeadboltActions
)(
  implicit
  ex: ExecutionContext
) extends AbstractController(components) with I18nSupport {

  def list = deadbolt.Pattern(value = "user.admin")() {
    implicit request =>
      Future.successful(Ok(Json.toJson("ALLOWED!!")))
  }
}
