package pl.laron.tutorial.akka.quartz

import akka.actor.{ActorSystem, Props}
import akka.testkit.TestKit
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension
import org.scalatest.{DiagrammedAssertions, FunSpecLike}
import org.scalatest.concurrent.Eventually

class QuartzFailOnSharingItByTwoActorsTest extends TestKit(ActorSystem("QuartzFailOnSharingItByTwoActorsTest"))
  with FunSpecLike with Eventually with DiagrammedAssertions {
  val scheduler: QuartzSchedulerExtension = QuartzSchedulerExtension(system)

  it("confirm that its not safe to share scheduler across actors") {
    system.actorOf(Props(new QuartzActor1(scheduler)))
//    Thread.sleep(1000)
    system.actorOf(Props(new QuartzActor2(scheduler)))

    eventually {
      assert(scheduler.runningJobs.size == 2)
    }
  }

}
