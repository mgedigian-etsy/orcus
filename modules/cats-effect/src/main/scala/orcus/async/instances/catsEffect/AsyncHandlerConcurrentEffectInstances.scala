package orcus.async.instances.catsEffect

import cats.effect.Async
import orcus.async.AsyncHandler
private[catsEffect] trait AsyncHandlerConcurrentEffectInstances {
  implicit def handleConcurrentEffect[F[_]](implicit F: Async[F]): AsyncHandler[F] =
    new AsyncHandler[F] {
      def handle[A](callback: AsyncHandler.Callback[A], cancel: => Unit): F[A] =
        F.async[A] { cb =>
          callback(cb); // orcus's callback's registration is `=> Unit`
          // but CE3's is Option[F[Unit]]
          F.delay(Some(F.delay(cancel)))
        }
    }
}
