package orcus.async.instances.catsEffect

import java.util.concurrent.CompletableFuture

import cats.effect.IO
import cats.effect.testing.scalatest.EffectTestSupport
import cats.effect.unsafe
import orcus.async._
import orcus.async.implicits._
import orcus.async.instances.catsEffect.concurrent._
import org.scalatest.flatspec.AnyFlatSpec

import scala.concurrent.ExecutionContext

class CatsEffectConcurrentHandlerSpec extends AnyFlatSpec with AsyncSpec with EffectTestSupport {
  implicit val executionContext: ExecutionContext = ExecutionContext.Implicits.global
  implicit val ioRuntime: unsafe.IORuntime        = cats.effect.unsafe.IORuntime.global

  it should "convert to a IO" in {
    def run = Par[CompletableFuture, IO].parallel(CompletableFuture.completedFuture(10))
    assert(10 === run.unsafeRunSync())
  }
  it should "convert to a failed IO" in {
    def run = Par[CompletableFuture, IO].parallel(failedFuture[Int](new Exception))
    assertThrows[Exception](run.unsafeRunSync())
  }
  it should "convert to a cancelable IO" in {
    val f = blockedFuture[Int]
    Par[CompletableFuture, IO].parallel(f).start.flatMap(_.cancel).unsafeRunSync()
    assert(f.isCancelled)
  }
}
