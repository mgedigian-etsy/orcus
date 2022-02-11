package orcus.async.instances.catsEffect

import java.util.concurrent.CompletableFuture

import cats.effect.IO
import cats.effect.testing.scalatest.AsyncIOSpec
import orcus.async._
import orcus.async.implicits._
import orcus.async.instances.catsEffect.effect._
import org.scalatest.flatspec.AsyncFlatSpec

class CatsEffectHandlerSpec extends AsyncFlatSpec with AsyncSpec with AsyncIOSpec {
  it should "convert to a IO" in {
    def run = Par[CompletableFuture, IO].parallel(CompletableFuture.completedFuture(10))
    assert(10 === run.unsafeRunSync())
  }
  it should "convert to a failed IO" in {
    def run = Par[CompletableFuture, IO].parallel(failedFuture[Int](new Exception))
    assertThrows[Exception](run.unsafeRunSync())
  }
}
