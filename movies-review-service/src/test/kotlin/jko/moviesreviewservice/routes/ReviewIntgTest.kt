package jko.moviesreviewservice.routes

import jko.moviesreviewservice.domain.Review
import jko.moviesreviewservice.repository.ReviewReactiveRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@TestPropertySource(properties = ["spring.mongodb.embedded.version=3.0.0"])
class ReviewIntgTest(
    @Autowired private val webTestClient: WebTestClient,
    @Autowired private val reviewReactiveRepository: ReviewReactiveRepository
) {


    @BeforeEach
    internal fun setUp() {
        val reviews = listOf(
            Review(null, 1L, "good", 9.0),
            Review(null, 2L, "so good", 9.0),
            Review(null, 3L, "awesome", 9.0),
        )
        reviewReactiveRepository.saveAll(reviews)
            .blockLast()
    }

    @AfterEach
    internal fun tearDown() {
        reviewReactiveRepository.deleteAll().block()
    }

    @Test
    internal fun addReview() {
        // given
        val review = Review(null, 1L, "good", 9.0)

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
                assertNotNull(savedReview)
                assertNotNull(savedReview?.movieInfoId)
            }
    }

    companion object {
        const val REVIEWS_URL = "/v1/reviews"
    }
}
