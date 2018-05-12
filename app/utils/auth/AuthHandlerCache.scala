package utils.auth

import be.objectify.deadbolt.scala.cache.HandlerCache
import be.objectify.deadbolt.scala.{ DeadboltHandler, HandlerKey }
import javax.inject.Inject

class AuthHandlerCache @Inject() (defaultHandler: DeadboltHandler) extends HandlerCache {
  // HandlerKeys is an user-defined object, containing instances of a case class that extends HandlerKey
  val handlers: Map[Any, DeadboltHandler] = Map(HandlerKeys.defaultHandler -> defaultHandler)

  // Get the default handler.
  override def apply(): DeadboltHandler = defaultHandler

  // Get a named handler
  override def apply(handlerKey: HandlerKey): DeadboltHandler = handlers(handlerKey)
}

object HandlerKeys {

  case class defaultHandler() extends be.objectify.deadbolt.scala.HandlerKey

}