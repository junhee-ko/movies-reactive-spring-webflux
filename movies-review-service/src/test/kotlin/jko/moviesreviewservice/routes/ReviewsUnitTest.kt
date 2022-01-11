package jko.moviesreviewservice.routes

import jko.moviesreviewservice.domain.Review
import jko.moviesreviewservice.exceptionhandler.GlobalErrorHandler
import jko.moviesreviewservice.handler.ReviewHandler
import jko.moviesreviewservice.repository.ReviewReactiveRepository
import jko.moviesreviewservice.router.ReviewRouter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.isA
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

@WebFluxTest
@ContextConfiguration(classes = [ReviewRouter::class, ReviewHandler::class, GlobalErrorHandler::class])
@AutoConfigureWebTestClient
class ReviewsUnitTest {

    @MockBean
    private lateinit var reviewReactiveRepository: ReviewReactiveRepository

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Test
    fun addReview() {
        // given
        val review = Review(null, 1L, "good", 9.0)

        `when`(reviewReactiveRepository.save(isA()))
            .thenReturn(
                Mono.just(Review("abc", 1L, "good", 9.0))
            )

        // when
        webTestClient
            .post()
            .uri(REVIEWS_URL)
            .bodyValue(review)
            .exchange()
            .expectStatus()
            .isCreated
            .expectBody(Review::class.java)
            .consumeWith {
                val savedReview = it.responseBody
                Assertions.assertNotNull(savedReview)
                Assertions.assertNotNull(savedReview?.reviewId)
            }

        // then
    }

    @Test
    fun addReviewValidation() {
        // given
        val review = Review(null, null, "good", -9.0)

        `when`(reviewReactiveRepository.save(isA()))
            .thenReturn(
                Mono.just(Review("abc", 1L, "good", 9.0))
            )

        // when
        webTestClient
            .post()
            .uri(REVIEWS_URL)
            .bodyValue(review)
            .exchange()
            .expectStatus()
            .isBadRequest
            .expectBody(String::class.java)
            .isEqualTo("rating.movieInfoId: must not be null, rating.negative: please pass a non-negative value")

        // then
    }


    companion object {
        const val REVIEWS_URL = "/v1/reviews"
    }
}
