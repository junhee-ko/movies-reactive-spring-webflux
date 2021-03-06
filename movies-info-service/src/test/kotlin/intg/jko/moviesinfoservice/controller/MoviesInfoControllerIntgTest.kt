package jko.moviesinfoservice.controller

import jko.moviesinfoservice.domain.MovieInfo
import jko.moviesinfoservice.repository.MovieInfoRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.time.LocalDate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@TestPropertySource(properties = ["spring.mongodb.embedded.version=3.0.0"])
class MoviesInfoControllerIntgTest(
    @Autowired private val movieInfoRepository: MovieInfoRepository,
    @Autowired private val webTestClient: WebTestClient
) {

    @BeforeEach
    fun setUp() {
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

        movieInfoRepository.saveAll(movieInfos)
            .blockLast()
    }

    @AfterEach
    fun tearDown() {
        movieInfoRepository.deleteAll().block()
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

        // when
        webTestClient
            .post()
            .uri("/v1/movieinfos")
            .bodyValue(movieInfo)
            .exchange()
            .expectStatus()
            .isCreated
            .expectBody<MovieInfo>()
            .consumeWith {
                val savedMovieInfo = it.responseBody
                assertNotNull(savedMovieInfo)
                assertNotNull(savedMovieInfo?.movieInfoId)
            }

        // then
    }

    @Test
    fun getAllMovieInfos() {
        // given

        // when
        webTestClient
            .get()
            .uri("/v1/movieinfos")
            .exchange()
            .expectStatus()
            .is2xxSuccessful
            .expectBodyList(MovieInfo::class.java)
            .hasSize(3)

        // then
    }

    @Test
    fun getAllMovieInfos_stream() {
        // given
        val movieInfo = MovieInfo(
            movieInfoId = null,
            name = "Batman Begins 2",
            year = 2005,
            cast = listOf("Christian Bale", "Michael Cane"),
            releaseDate = LocalDate.of(2000, 11, 14)
        )

        webTestClient
            .post()
            .uri("/v1/movieinfos")
            .bodyValue(movieInfo)
            .exchange()
            .expectStatus()
            .isCreated
            .expectBody<MovieInfo>()
            .consumeWith {
                val savedMovieInfo = it.responseBody
                assertNotNull(savedMovieInfo)
                assertNotNull(savedMovieInfo?.movieInfoId)
            }

        // when
        val moviesStreamFlux: Flux<MovieInfo> = webTestClient
            .get()
            .uri("/v1/movieinfos/stream")
            .exchange()
            .expectStatus()
            .is2xxSuccessful
            .returnResult(MovieInfo::class.java)
            .responseBody

        // then
        StepVerifier.create(moviesStreamFlux)
            .assertNext { assertNotNull(it.movieInfoId) }
            .thenCancel() // streaming endpoint ??????, long live connection ??????. ????????? thenCancel ??? ??????.
            .verify()
    }

    @Test
    fun getAllMovieInfoByYear() {
        // given
        val uri = UriComponentsBuilder.fromUriString("/v1/movieinfos")
            .queryParam("year", 2005)
            .buildAndExpand().toUri()

        // when
        webTestClient
            .get()
            .uri(uri)
            .exchange()
            .expectStatus()
            .is2xxSuccessful
            .expectBodyList(MovieInfo::class.java)
            .hasSize(1)

        // then
    }

    @Test
    fun getMovieInfoById() {
        // given
        val movieInfoId = "abc"

        // when
        webTestClient
            .get()
            .uri("/v1/movieinfos/{id}", movieInfoId)
            .exchange()
            .expectStatus()
            .is2xxSuccessful
            .expectBody()
            .jsonPath("$.name").isEqualTo("Uncle")
//            .expectBody()
//            .consumeWith {
//                val movieInfo = it.responseBody
//                assertNotNull(movieInfo)
//            }

        // then
    }

    @Test
    fun updateMovieInfo() {
        // given
        val movieInfoId = "abc"
        val movieInfo = MovieInfo(
            movieInfoId = null,
            name = "My Name",
            year = 2005,
            cast = listOf("Christian Bale", "Michael Cane"),
            releaseDate = LocalDate.of(2000, 11, 14)
        )

        // when
        webTestClient
            .put()
            .uri("/v1/movieinfos/{id}", movieInfoId)
            .bodyValue(movieInfo)
            .exchange()
            .expectStatus()
            .is2xxSuccessful
            .expectBody<MovieInfo>()
            .consumeWith {
                val updatedMovieInfo = it.responseBody
                assertNotNull(updatedMovieInfo)
                assertNotNull(updatedMovieInfo?.movieInfoId)
                assertEquals("My Name", updatedMovieInfo?.name)
            }

        // then
    }

    @Test
    fun deleteMovieInfo() {
        // given
        val movieInfoId = "abc"

        // when
        webTestClient
            .delete()
            .uri("/v1/movieinfos/{id}", movieInfoId)
            .exchange()
            .expectStatus()
            .isNoContent
            .expectBody(Void::class.java)

        // then
    }

    @Test
    fun updateMovieInfoNotFound() {
        // given
        val movieInfoId = "def"
        val movieInfo = MovieInfo(
            movieInfoId = null,
            name = "My Name",
            year = 2005,
            cast = listOf("Christian Bale", "Michael Cane"),
            releaseDate = LocalDate.of(2000, 11, 14)
        )

        // when
        webTestClient
            .put()
            .uri("/v1/movieinfos/{id}", movieInfoId)
            .bodyValue(movieInfo)
            .exchange()
            .expectStatus()
            .isNotFound
            .expectBody(MovieInfo::class.java)

        // then
    }
}
