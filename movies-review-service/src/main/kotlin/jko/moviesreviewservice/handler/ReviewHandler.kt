package jko.moviesreviewservice.handler

import jko.moviesreviewservice.domain.Review
import jko.moviesreviewservice.exception.ReviewDataException
import jko.moviesreviewservice.exception.ReviewNotFoundException
import jko.moviesreviewservice.repository.ReviewReactiveRepository
import org.jboss.logging.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks
import java.util.*
import javax.validation.ConstraintViolation
import javax.validation.Validator

@Component
class ReviewHandler(
    private val reviewReactiveRepository: ReviewReactiveRepository,
    private val validator: Validator
) {

    private val logger = Logger.getLogger(this::class.java)
    val reviewsSink: Sinks.Many<Review> = Sinks.many().replay().latest()

    fun addReview(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(Review::class.java)
            .doOnNext(this::validate)
            .flatMap { review -> reviewReactiveRepository.save(review) }
            .doOnNext { savedReview -> reviewsSink.tryEmitNext(savedReview) }
            .flatMap { savedReview -> ServerResponse.status(HttpStatus.CREATED).bodyValue(savedReview) }
    }

    private fun validate(review: Review) {
        val constraintViolations: MutableSet<ConstraintViolation<Review>> = validator.validate(review)
        logger.info("constraintViolations: $constraintViolations")

        if (constraintViolations.size > 0) {
            val errorMessage = constraintViolations
                .map { it.message }
                .sorted()
                .joinToString()

            throw ReviewDataException(errorMessage)
        }
    }

    fun getReviews(request: ServerRequest): Mono<ServerResponse> {
        val movieInfoId: Optional<String> = request.queryParam("movieInfoId")

        return if (movieInfoId.isPresent) {
            val reviewsFlux = reviewReactiveRepository.findReviewsByMovieInfoId(
                movieInfoId.map { it.toLong() }.get()
            )

            buildReviewsResponse(reviewsFlux)
        } else {
            val reviewsFlux = reviewReactiveRepository.findAll()

            buildReviewsResponse(reviewsFlux)
        }

    }

    private fun buildReviewsResponse(reviewsFlux: Flux<Review>) =
        ServerResponse.ok().body(reviewsFlux, Review::class.java)

    fun updateReview(request: ServerRequest): Mono<ServerResponse> {
        val reviewId = request.pathVariable("id")
        val existingReview = reviewReactiveRepository.findById(reviewId)
            .switchIfEmpty(Mono.error(ReviewNotFoundException("Review not found for the given Review id $reviewId")))

        return existingReview
            .flatMap { review ->
                request.bodyToMono(Review::class.java)
                    .map { reqReview ->
                        review.comment = reqReview.comment
                        review.rating = reqReview.rating
                        review
                    }
                    .flatMap { reviewReactiveRepository.save(it) }
                    .flatMap { savedReview -> ServerResponse.ok().bodyValue(savedReview) }
            }
//            .switchIfEmpty(ServerResponse.notFound().build())

    }

    fun deleteReview(request: ServerRequest): Mono<ServerResponse> {
        val reviewId = request.pathVariable("id")
        val existingReview = reviewReactiveRepository.findById(reviewId)

        return existingReview
            .flatMap { review -> reviewReactiveRepository.deleteById(reviewId) }
            .then(ServerResponse.noContent().build())
    }

    fun getReviewsStream(request: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_NDJSON)
            .body(reviewsSink.asFlux(), Review::class.java)
            .log()
    }
}
