package pl.laron.tutorial.akka.streams

import java.nio.file.{Path, Paths}

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, IOResult}
import akka.stream.scaladsl.{FileIO, Sink, Source}
import akka.testkit.TestKit
import org.scalatest.FunSpecLike

import scala.concurrent.{ExecutionContextExecutor, Future}

class MergeSortedStreamsTest extends TestKit(ActorSystem("MergeSortedStreamsTest")) with FunSpecLike {
  private implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  private implicit val materializer: ActorMaterializer = ActorMaterializer()

  it("should produce merged elements sorted by timestamp") {
    println("Merging a  and b")
    val sourceA = Source(List(1, 2, 3, 5, 7))
    val sourceB = Source(List(2, 4, 6, 8))

    sourceA.mergeSorted(sourceB).runWith(Sink.foreach(println))
    //prints 1, 2, 3, 4, 5, 6, 7, 8

    println("Merge a with c")
    val sourceC = Source(List(20, 1, 1, 1))

    sourceA.mergeSorted(sourceC).runWith(Sink.foreach(println))
    case class ReplayFixture(timestamp: Long, paylod: Any)

    val foreach: Future[IOResult] = FileIO.fromPath(Paths.get(""))
      .to(Sink.ignore)
      .run()
  }
}
