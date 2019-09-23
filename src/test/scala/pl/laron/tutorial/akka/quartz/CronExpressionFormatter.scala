package pl.laron.tutorial.akka.quartz

import java.time.{Instant, ZoneId}
import java.time.format.DateTimeFormatter

object CronExpressionFormatter {
  private val CronFormat: DateTimeFormatter =
    DateTimeFormatter.ofPattern("ss mm HH dd MM ? yyyy").withZone(ZoneId.of("UTC"))

  def formatCronExpression(time: Instant): String = {
    CronFormat.format(time)
  }
}