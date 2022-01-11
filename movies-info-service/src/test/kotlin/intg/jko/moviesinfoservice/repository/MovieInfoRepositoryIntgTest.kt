package jko.moviesinfoservice.repository

import jko.moviesinfoservice.domain.MovieInfo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.time.LocalDate

@DataMongoTest
@ActiveProfiles("test")
@TestPropertySource(properties = ["spring.mongodb.embedded.version=3.0.0"])
class MovieInfoRepositoryIntgTest(
    @Autowired val movieInfoRepository: MovieInfoRepository
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
    fun findAll() {
        // given

        // when
        val moviesInfoFlux: Flux<MovieInfo> = movieInfoRepository.findAll().log()

        // then
        StepVerifier.create(moviesInfoFlux)
            .expectNextCount(3)
            .verifyComplete()
    }

    @Test
    fun findById() {
        // given

        // when
        val moviesInfoMono = movieInfoRepository.findById("abc").log()

        // then
        StepVerifier.create(moviesInfoMono)
            .assertNext { assertEquals("Uncle", it.name) }
            .verifyComplete()
    }

    @Test
    fun save() {
        // given
        val movieInfo = MovieInfo(
            movieInfoId = null,
            name = "Look at me",
            year = 2015,
            cast = listOf("jko", "Ko"),
            releaseDate = LocalDate.of(2000, 11, 14)
        )

        // when
        val moviesInfoMono = movieInfoRepository.save(movieInfo).log()

        // then
        StepVerifier.create(moviesInfoMono)
            .assertNext {
                assertNotNull(it.movieInfoId)
                assertEquals("Look at me", it.name)
            }
            .verifyComplete()
    }

    @Test
    fun update() {
        // given
        val movieInfo: MovieInfo? = movieInfoRepository.findById("abc").block()
        movieInfo?.year = 2021

        // when
        val moviesInfoMono = movieInfoRepository.save(movieInfo!!).log()

        // then
        StepVerifier.create(moviesInfoMono)
            .assertNext {
                assertEquals(2021, it.year)
            }
            .verifyComplete()
    }

    @Test
    fun delete() {
        // given

        // when
        movieInfoRepository.deleteById("abc").block()

        // then
        val moviesInfoFlux = movieInfoRepository.findAll().log()
        StepVerifier.create(moviesInfoFlux)
            .expectNextCount(2)
            .verifyComplete()
    }

    @Test
    fun findByYear() {
        // given

        // when
        val moviesInfoFlux: Flux<MovieInfo> = movieInfoRepository.findByYear(2005).log()

        // then
        StepVerifier.create(moviesInfoFlux)
            .expectNextCount(1)
            .verifyComplete()
    }
}
