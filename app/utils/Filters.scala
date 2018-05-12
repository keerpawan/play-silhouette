package utils

import javax.inject.Inject
import play.api.http.HttpFilters
import play.api.mvc.EssentialFilter
import play.filters.csrf.CSRFFilter
import play.filters.headers.SecurityHeadersFilter
import utils.filters.LoggingFilter

/**
 * Provides filters.
 */
class Filters @Inject() (csrfFilter: CSRFFilter, securityHeadersFilter: SecurityHeadersFilter,
  log: LoggingFilter) extends HttpFilters {
  override def filters: Seq[EssentialFilter] = Seq(csrfFilter, securityHeadersFilter, log)
}
