package jko.moviesservice

import com.github.tomakehurst.wiremock.client.WireMock.*
import jko.moviesservice.domain.Movie
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.reactive.server.EntityExchangeResult
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@AutoConfigureWireMock(port = 8084)
@TestPropertySource(
    properties = [
        "restClient.moviesInfoUrl=http://localhost:8084/v1/movieinfos",
        "restClient.reviewsUrl=http://localhost:8084/v1/reviews"
    ]
)
class MoviesControllerIntgTest(
    @Autowired private val webTestClient: WebTestClient
) {


    @Test
    internal fun retrieveMovieById() {
        // given
        val movieId = "abc"
        stubFor(
            get(urlEqualTo("/v1/movieinfos/$movieId"))
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("movieinfo.json")
                )
        )
        stubFor(
            get(urlPathEqualTo("/v1/reviews"))
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("reviews.json")
                )
        )

        // when
        webTestClient
            .get()
            .uri("/v1/movies/{id}", movieId)
            .exchange()
            .expectStatus().isOk
            .expectBody(Movie::class.java)
            .consumeWith { it: EntityExchangeResult<Movie> ->
                val movie = it.responseBody
                assertNotNull(movie)
                assertEquals(2, movie!!.reviews.size)
                assertEquals("Batman Begins", movie.movieInfo.name)
            }

        // then

    }
}
