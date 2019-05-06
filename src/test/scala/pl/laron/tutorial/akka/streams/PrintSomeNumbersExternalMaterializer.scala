package pl.laron.tutorial.akka.streams

import akka.actor.{Actor, ActorLogging, ActorRef, ReceiveTimeout}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration._

class PrintSomeNumbersExternalMaterializer(implicit val materializer: ActorMaterializer) extends Actor with ActorLogging {
  private implicit val executionContext: ExecutionContextExecutor = context.system.dispatcher
  private val parentRef: ActorRef = context.parent

  aStream

  private def aStream = {
    Source.tick(1 seconds, 1 second, 1)
      .scan(1)(_ + _)
      .map(_.toString)
      .runForeach(
        parentRef ! _
      )
      .map(_ => self ! "done")
  }

  context.setReceiveTimeout(5 seconds)

  override def preStart(): Unit = {
    log.info("Starting numbers")
  }

  override def receive: Receive = {
    case "done" =>
      log.info("Done")
      context.stop(self)
    case ReceiveTimeout =>
      log.info("Receive timeout, stoppping actor")
      context.stop(self)
  }
}
