package pl.laron.tutorial.akka.quartz

import java.time.Instant
import java.util.TimeZone

import akka.actor.{Actor, ActorLogging, ActorPath}
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension

class QuartzActor1(scheduler: QuartzSchedulerExtension) extends Actor with ActorLogging {
  private val schedule_id = "QuartzActor1_schedule1"
  scheduler.createSchedule(schedule_id,
    None,
    CronExpressionFormatter.formatCronExpression(Instant.now.plusSeconds(10)),
    None,
    TimeZone.getTimeZone("UTC"))
  scheduler.schedule(schedule_id, self, Ping)

  override def receive: Receive = {
    case Ping => log.info("Ping received actor 1")
  }
}
