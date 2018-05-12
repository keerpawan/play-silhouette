package utils.auth

import be.objectify.deadbolt.scala.models.Subject
import be.objectify.deadbolt.scala.{ AuthenticatedRequest, DeadboltHandler, DynamicResourceHandler }
import com.mohiva.play.silhouette.api.{ LoginInfo, RequestProvider, Silhouette }
import javax.inject.Inject
import utils.Logger
import play.api.mvc.Results._
import play.api.mvc.{ Request, Result }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class AuthHandler @Inject() (silhouette: Silhouette[DefaultEnv]) extends DeadboltHandler with Logger {
  override def beforeAuthCheck[A](request: Request[A]): Future[Option[Result]] = Future.successful(None)

  override def getSubject[A](request: AuthenticatedRequest[A]): Future[Option[Subject]] = if (request.subject.isDefined) {
    logRequest(request, request.subject)
    Future.successful(request.subject)
  } else {
    // this else branch is taken from com.mohiva.play.silhouette.api.RequestHandlerBuilder
    silhouette.env.authenticatorService.retrieve(request).flatMap {
      // A valid authenticator was found so we retrieve also the identity
      case Some(a) if a.isValid =>
        silhouette.env.identityService.retrieve(a.loginInfo).map { i =>
          logRequest(request, i.map(_.subject))
          i.map(_.subject)
        }
      // An invalid authenticator was found so we needn't retrieve the identity
      case _ =>
        logRequest(request, None)
        Future.successful(None)
    }
  }

  // this whole function is taken from com.mohiva.play.silhouette.api.RequestHandlerBuilder
  private def handleRequestProviderAuthentication[B](implicit request: Request[B]): Future[Option[LoginInfo]] = {
    def auth(providers: Seq[RequestProvider]): Future[Option[LoginInfo]] = {
      providers match {
        case Nil => Future.successful(None)
        case h :: t => h.authenticate(request).flatMap {
          case Some(i) => Future.successful(Some(i))
          case None => if (t.isEmpty) Future.successful(None) else auth(t)
        }
      }
    }

    logRequest(request, None)
    auth(silhouette.env.requestProviders)
  }

  override def onAuthFailure[A](request: AuthenticatedRequest[A]): Future[Result] = {
    logRequest(request, None)
    Future.successful(Unauthorized)
  }

  override def getDynamicResourceHandler[A](request: Request[A]): Future[Option[DynamicResourceHandler]] = {
    logRequest(request, None)
    Future.successful(None)
  }

  private def logRequest[A](request: AuthenticatedRequest[A], subject: Option[Subject]): Unit = {
    // To log json request data
    if (request.getQueryString("reqId").isDefined) // ignoring queries without reqId
      logger.info(s"[${request.getQueryString("reqId").getOrElse("")}] Request : ${request.method} ${request.uri} " +
        s"requested by ${subject.map(_.identifier).getOrElse("Anonymous")} with body ${request.body}")
  }

  private def logRequest[A](request: Request[A], subject: Option[Subject]): Unit = {
    // To log json request data
    if (request.getQueryString("reqId").isDefined) // ignoring queries without reqId
      logger.info(s"[${request.getQueryString("reqId").getOrElse("")}] Request : ${request.method} ${request.uri} " +
        s"requested by ${subject.map(_.identifier).getOrElse("Anonymous")} with body ${request.body}")
  }
}