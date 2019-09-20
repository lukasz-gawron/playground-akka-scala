package pl.laron.tutorial.scala

import java.time.Instant

import org.scalatest.FunSpec

import scala.concurrent.duration.{Deadline, FiniteDuration}
import scala.compat.java8.DurationConverters._

class DeadlineTest extends FunSpec {
  it("shows how deadline class may be used for waiting from some time to take action") {
    val now = Instant.now()
    val kickOffGame1 = now.plusSeconds(10)
    val untilPostMatch: FiniteDuration = java.time.Duration.between(now, kickOffGame1.plusSeconds(5))
      .toScala
    val deadlineUntilPostMatchShouldBeTriggered = untilPostMatch.fromNow
    while(deadlineUntilPostMatchShouldBeTriggered.hasTimeLeft()) {
      println("Time left" + deadlineUntilPostMatchShouldBeTriggered.timeLeft.toSeconds)
      println("Has time left: " + deadlineUntilPostMatchShouldBeTriggered.hasTimeLeft())
      println("Until post match: " + untilPostMatch)
      Thread.sleep(1000)
    }
  }
}
