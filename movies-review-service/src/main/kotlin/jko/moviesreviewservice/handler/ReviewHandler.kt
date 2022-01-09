package jko.moviesreviewservice.handler

import jko.moviesreviewservice.domain.Review
import jko.moviesreviewservice.repository.ReviewReactiveRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class ReviewHandler(
    private val reviewReactiveRepository: ReviewReactiveRepository
) {

    fun addReview(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(Review::class.java)
            .flatMap { review -> reviewReactiveRepository.save(review) }
            .flatMap { savedReview -> ServerResponse.status(HttpStatus.CREATED).bodyValue(savedReview) }
    }
}
