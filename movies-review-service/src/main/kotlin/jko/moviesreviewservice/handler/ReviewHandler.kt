package jko.moviesreviewservice.handler

import jko.moviesreviewservice.domain.Review
import jko.moviesreviewservice.repository.ReviewReactiveRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Component
class ReviewHandler(
    private val reviewReactiveRepository: ReviewReactiveRepository
) {

    fun addReview(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(Review::class.java)
            .flatMap { review -> reviewReactiveRepository.save(review) }
            .flatMap { savedReview -> ServerResponse.status(HttpStatus.CREATED).bodyValue(savedReview) }
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

    }

    fun deleteReview(request: ServerRequest): Mono<ServerResponse> {
        val reviewId = request.pathVariable("id")
        val existingReview = reviewReactiveRepository.findById(reviewId)

        return existingReview
            .flatMap { review -> reviewReactiveRepository.deleteById(reviewId) }
            .then(ServerResponse.noContent().build())
    }
}
