package pl.laron.tutorial.akka.quartz

import java.time.{Instant, ZoneId}
import java.time.format.DateTimeFormatter
import java.util.TimeZone

import akka.actor.{Actor, ActorLogging}
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension

class QuartzActor2(scheduler: QuartzSchedulerExtension) extends Actor with ActorLogging {

  private val schedule_id = "QuartzActor2_schedule1"
  scheduler.createSchedule(schedule_id,
    None,
    CronExpressionFormatter.formatCronExpression(Instant.now.plusSeconds(10)),
    None,
    TimeZone.getTimeZone("UTC"))
  scheduler.schedule(schedule_id, self, Ping)

  override def receive: Receive = {
    case Ping => log.info("Ping received actor 2")
  }
}
