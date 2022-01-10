package jko.moviesreviewservice.repository

import jko.moviesreviewservice.domain.Review
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux

interface ReviewReactiveRepository : ReactiveMongoRepository<Review, String> {

    fun findReviewsByMovieInfoId(movieInfoId: Long): Flux<Review>
}
