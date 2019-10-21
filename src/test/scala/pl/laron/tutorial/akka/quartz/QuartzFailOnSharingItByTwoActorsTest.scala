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

    /**
      * on logs should be "No matching quartz configuration found for schedule 'QuartzActor2_schedule1' due to QuartzScheduler
      * is not safe to use in more than one actor
      *
      * https://github.com/enragedginger/akka-quartz-scheduler/issues/33
      *
      * akka.actor.ActorInitializationException: akka://QuartzFailOnSharingItByTwoActorsTest/user/$b: exception during creation
      * at akka.actor.ActorInitializationException$.apply(Actor.scala:193)
      * at akka.actor.ActorCell.create(ActorCell.scala:669)
      * at akka.actor.ActorCell.invokeAll$1(ActorCell.scala:523)
      * at akka.actor.ActorCell.systemInvoke(ActorCell.scala:545)
      * at akka.dispatch.Mailbox.processAllSystemMessages(Mailbox.scala:283)
      * at akka.dispatch.Mailbox.run(Mailbox.scala:224)
      * at akka.dispatch.Mailbox.exec(Mailbox.scala:235)
      * at akka.dispatch.forkjoin.ForkJoinTask.doExec(ForkJoinTask.java:260)
      * at akka.dispatch.forkjoin.ForkJoinPool$WorkQueue.runTask(ForkJoinPool.java:1339)
      * at akka.dispatch.forkjoin.ForkJoinPool.runWorker(ForkJoinPool.java:1979)
      * at akka.dispatch.forkjoin.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:107)
      * Caused by: java.lang.IllegalArgumentException: No matching quartz configuration found for schedule 'QuartzActor2_schedule1'
      * at com.typesafe.akka.extension.quartz.QuartzSchedulerExtension.scheduleInternal(QuartzSchedulerExtension.scala:370)
      * at com.typesafe.akka.extension.quartz.QuartzSchedulerExtension.schedule(QuartzSchedulerExtension.scala:305)
      * at pl.laron.tutorial.akka.quartz.QuartzActor2.<init>(QuartzActor2.scala:18)
      * at pl.laron.tutorial.akka.quartz.QuartzFailOnSharingItByTwoActorsTest$$anonfun$1$$anonfun$apply$2.apply(QuartzFailOnSharingItByTwoActorsTest.scala:16)
      * at pl.laron.tutorial.akka.quartz.QuartzFailOnSharingItByTwoActorsTest$$anonfun$1$$anonfun$apply$2.apply(QuartzFailOnSharingItByTwoActorsTest.scala:16)
      * at akka.actor.TypedCreatorFunctionConsumer.produce(IndirectActorProducer.scala:87)
      * at akka.actor.Props.newActor(Props.scala:212)
      * at akka.actor.ActorCell.newActor(ActorCell.scala:624)
      * at akka.actor.ActorCell.create(ActorCell.scala:650)
      * ... 9 more
      */
    eventually {
      assert(scheduler.runningJobs.size == 1)
    }
  }

}
