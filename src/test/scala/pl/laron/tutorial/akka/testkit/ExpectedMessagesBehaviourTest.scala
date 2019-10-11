package pl.laron.tutorial.akka.testkit

import akka.actor.ActorSystem
import akka.testkit.{TestKit, TestProbe}
import org.scalatest.FunSpecLike
import pl.laron.tutorial.akka.quartz.Ping
import scala.concurrent.duration._

class ExpectedMessagesBehaviourTest extends TestKit(ActorSystem("ExpectedMessagesBehaviourTest")) with FunSpecLike {
  // implementation of expectedMsg works on queue-like structure maintaining messages
  // received by actor in chronological order, once message is retreived from queue,
  // is not available for other asserts anymore
  it("TestProbe receives messages send to probe BEFORE expected calls are done") {
    val probe = TestProbe()
    probe.ref ! Ping

    probe.expectMsg(100 millis, Ping)
  }

  it("removes already asserted msg from internal queue so other asserts won't work on already processed msg") {
    val probe = TestProbe()
    probe.ref ! Ping

    probe.expectMsg(100 millis, Ping)
    assertThrows[AssertionError] {
      probe.expectMsg(100 millis, Ping)
    }
  }

  it("expectNoMessage fail if message received before its call, wasn't asserted in the past") {
    val probe = TestProbe()
    probe.ref ! Ping

    assertThrows[AssertionError] {
      probe.expectNoMessage(100 millis)
    }
  }
}
