package jko.moviesservice.util

import jko.moviesservice.exception.MovieInfoServerException
import jko.moviesservice.exception.ReviewsServerException
import reactor.core.Exceptions
import reactor.util.retry.Retry
import reactor.util.retry.RetryBackoffSpec
import java.time.Duration

class RetryUtil {

    companion object {
        fun retrySpec(): RetryBackoffSpec {
            return Retry.fixedDelay(3, Duration.ofSeconds(1))
                .filter { ex -> ex is MovieInfoServerException || ex is ReviewsServerException}
                .onRetryExhaustedThrow { retryBackOffSpec: RetryBackoffSpec, retrySignal: Retry.RetrySignal ->
                    Exceptions.propagate(retrySignal.failure())
                }
        }
    }
}
