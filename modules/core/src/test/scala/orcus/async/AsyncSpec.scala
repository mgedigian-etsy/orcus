package orcus.async

import java.util.concurrent.CompletableFuture
import java.util.function.Supplier

import org.scalatest.AsyncTestSuite

import scala.concurrent._

trait AsyncSpec { _: AsyncTestSuite =>

  def failedFuture[A](e: Throwable): CompletableFuture[A] =
    CompletableFuture.failedFuture(e)

  def blockedFuture[A]: CompletableFuture[A] =
    CompletableFuture.supplyAsync(new Supplier[A] {
      def get(): A = blocking { Thread.sleep(Int.MaxValue); fail() }
    })
}
