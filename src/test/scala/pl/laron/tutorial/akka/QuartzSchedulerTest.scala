package pl.laron.tutorial.akka

import java.time.{Instant, ZoneId}
import java.time.format.DateTimeFormatter
import java.util.TimeZone
import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.testkit.{TestKit, TestProbe}
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension
import org.scalatest.FunSpecLike

class QuartzSchedulerTest extends TestKit(ActorSystem("QuartzSchedulerTest")) with FunSpecLike {
  private val scheduler = QuartzSchedulerExtension(system)
  private val CronFormat: DateTimeFormatter =
    DateTimeFormatter.ofPattern("ss mm HH dd MM ? yyyy").withZone(ZoneId.of("UTC"))

  def formatCronExpression(time: Instant): String = {
    CronFormat.format(time)
  }

  it("start based on schedule in future") {
    val probe = TestProbe()
    scheduler
      .createSchedule(
        name = "test1",
        description = Some("from past"),
        cronExpression = CronFormat.format(Instant.now().plusSeconds(1)),
        calendar = None,
        timezone = TimeZone.getTimeZone("UTC"))

    scheduler.schedule(name = "test1", receiver = probe.ref, msg = Ping)

    probe.expectMsg(Ping)
  }

  case object Ping {}

}
