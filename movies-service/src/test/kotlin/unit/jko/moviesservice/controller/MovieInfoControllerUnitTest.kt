package jko.moviesservice.controller

import jko.moviesservice.domain.MovieInfo
import jko.moviesservice.service.MovieInfoService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.isA
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate

@WebFluxTest(controllers = [MoviesInfoController::class])
@AutoConfigureWebTestClient
class MovieInfoControllerUnitTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockBean
    private lateinit var movieInfoServiceMock: MovieInfoService

    @Test
    fun getAllMovieInfo() {
        val movieInfos = listOf(
            MovieInfo(
                movieInfoId = null,
                name = "Batman Begins",
                year = 2005,
                cast = listOf("Christian Bale", "Michael Cane"),
                releaseDate = LocalDate.of(2000, 11, 14)
            ),
            MovieInfo(
                movieInfoId = null,
                name = "PARASITE",
                year = 2021,
                cast = listOf("Park", "Bong"),
                releaseDate = LocalDate.of(2021, 2, 1)
            ),
            MovieInfo(
                movieInfoId = "abc",
                name = "Uncle",
                year = 2010,
                cast = listOf("Ko", "Junhee"),
                releaseDate = LocalDate.of(1990, 7, 20)
            )
        )


        `when`(movieInfoServiceMock.getAllMovieInfos()).thenReturn(Flux.fromIterable(movieInfos))

        webTestClient
            .get()
            .uri("/v1/movieinfos")
            .exchange()
            .expectStatus()
            .is2xxSuccessful
            .expectBodyList(MovieInfo::class.java)
            .hasSize(3)
    }

    @Test
    fun addMovieInfo() {
        // given
        val movieInfo = MovieInfo(
            movieInfoId = null,
            name = "Batman Begins 2",
            year = 2005,
            cast = listOf("Christian Bale", "Michael Cane"),
            releaseDate = LocalDate.of(2000, 11, 14)
        )

        `when`(movieInfoServiceMock.addMovieInfo(isA())).thenReturn(
            Mono.just(
                MovieInfo(
                    movieInfoId = "mockId",
                    name = "Batman Begins 2",
                    year = 2005,
                    cast = listOf("Christian Bale", "Michael Cane"),
                    releaseDate = LocalDate.of(2000, 11, 14)
                )
            )
        )

        // when
        webTestClient
            .post()
            .uri("/v1/movieinfos")
            .bodyValue(movieInfo)
            .exchange()
            .expectStatus()
            .isCreated
            .expectBody(MovieInfo::class.java)
            .consumeWith {
                val savedMovieInfo = it.responseBody
                Assertions.assertNotNull(savedMovieInfo)
                Assertions.assertNotNull(savedMovieInfo?.movieInfoId)
                assertEquals("mockId", savedMovieInfo?.movieInfoId)
            }

        // then
    }

    @Test
    fun updateMovieInfo() {
        // given
        val movieInfoId = "abc"
        val movieInfo = MovieInfo(
            movieInfoId = null,
            name = "Batman Begins 2",
            year = 2005,
            cast = listOf("Christian Bale", "Michael Cane"),
            releaseDate = LocalDate.of(2000, 11, 14)
        )

        `when`(movieInfoServiceMock.updateMovieInfo(isA(), isA())).thenReturn(
            Mono.just(
                MovieInfo(
                    movieInfoId = "mockId",
                    name = "Batman Begins 2",
                    year = 2005,
                    cast = listOf("Christian Bale", "Michael Cane"),
                    releaseDate = LocalDate.of(2000, 11, 14)
                )
            )
        )

        // when
        webTestClient
            .put()
            .uri("/v1/movieinfos/$movieInfoId")
            .bodyValue(movieInfo)
            .exchange()
            .expectStatus()
            .is2xxSuccessful
            .expectBody(MovieInfo::class.java)
            .consumeWith {
                val updatedMovieInfo = it.responseBody
                Assertions.assertNotNull(updatedMovieInfo)
                Assertions.assertNotNull(updatedMovieInfo?.movieInfoId)
                assertEquals("mockId", updatedMovieInfo?.movieInfoId)
            }

        // then
    }
}
