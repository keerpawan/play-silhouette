package utils.filters

import akka.stream._
import akka.util.ByteString
import javax.inject.Inject
import utils.Logger
import play.api.libs.streams.Accumulator
import play.api.mvc._

import scala.concurrent.ExecutionContext

class LoggingFilter @Inject() (implicit ec: ExecutionContext, mat: Materializer) extends EssentialFilter with Logger {
  def apply(nextFilter: EssentialAction) = new EssentialAction {

    def apply(requestHeader: RequestHeader) = {
      val startTime = System.currentTimeMillis

      val accumulator: Accumulator[ByteString, Result] = nextFilter(requestHeader)

      accumulator.map { result =>
        val endTime = System.currentTimeMillis
        val requestTime = endTime - startTime

        // To log json response data
        if (requestHeader.getQueryString("reqId").isDefined) // ignoring queries without reqId
          result.body.consumeData(mat).map { body =>
            logger.info(s"[${requestHeader.getQueryString("reqId").get}] Response : ${requestHeader.method} ${requestHeader.uri} took ${requestTime}ms and returned ${result.header.status} with body ${body.decodeString("UTF-8")}")
          }

        result.withHeaders("Request-Time" -> requestTime.toString)

      }
    }

  }

}