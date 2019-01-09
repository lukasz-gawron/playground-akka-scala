package pl.laron.tutorial.akka.streams

import akka.actor.{Actor, ActorLogging, ReceiveTimeout}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration._

class PrintSomeNumbers(implicit val materializer: ActorMaterializer) extends Actor with ActorLogging {
  private implicit val executionContext: ExecutionContextExecutor = context.system.dispatcher


  aStream

  private def aStream = {
    Source.tick(1 seconds, 1 second, 1)
      .scan(1)(_ + _)
      .map(_.toString)
      .runForeach(println)
      .map(_ => self ! "done")
  }

  context.setReceiveTimeout(5 seconds)

  override def preStart(): Unit = {
    println("Starting numbers")
  }

  override def receive: Receive = {
    case "done" =>
      println("Done")
      context.stop(self)
    case ReceiveTimeout =>
      context.system.log.info("Receive timeout")
      context.stop(self)
  }
}
