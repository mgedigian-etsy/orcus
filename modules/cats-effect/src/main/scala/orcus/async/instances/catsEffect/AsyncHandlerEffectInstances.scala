package orcus.async.instances.catsEffect

import cats.effect.Async
import orcus.async.AsyncHandler
private[catsEffect] trait AsyncHandlerEffectInstances {
  implicit def handleEffect[F[_]](implicit F: Async[F]): AsyncHandler[F] =
    new AsyncHandler[F] {
      def handle[A](callback: AsyncHandler.Callback[A], cancel: => Unit): F[A] =
        F.async_(callback)
    }
}
