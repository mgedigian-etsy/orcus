package orcus.async.future

import java.util.concurrent.CompletableFuture

import orcus.async.AsyncSpec
import orcus.async.Par
import orcus.async.implicits._
import orcus.async.instances.future._
import org.scalatest.flatspec.AsyncFlatSpec

import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration._

class ScalaFutureAsyncHandlerSpec extends AsyncFlatSpec with AsyncSpec {
  it should "convert to a Future" in {
    def run = Par[CompletableFuture, Future].parallel(CompletableFuture.completedFuture(10))
    assert(10 === Await.result(run, 3.seconds))
  }
  it should "convert to a failed Future" in {
    def run = Par[CompletableFuture, Future].parallel(failedFuture[Int](new Exception))
    assertThrows[Exception](Await.result(run, 3.seconds))
  }
}
