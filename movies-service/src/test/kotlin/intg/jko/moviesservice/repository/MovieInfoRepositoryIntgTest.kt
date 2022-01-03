package jko.moviesservice.repository

import jko.moviesservice.domain.MovieInfo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
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
internal class MovieInfoRepositoryIntgTest(
    @Autowired val movieInfoRepository: MovieInfoRepository
) {

    @BeforeEach
    internal fun setUp() {
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
    internal fun tearDown() {
        movieInfoRepository.deleteAll().block()
    }

    @Test
    internal fun findAll() {
        // given

        // when
        val moviesInfoFlux: Flux<MovieInfo> = movieInfoRepository.findAll().log()

        // then
        StepVerifier.create(moviesInfoFlux)
            .expectNextCount(3)
            .verifyComplete()
    }

    @Test
    internal fun findById() {
        // given

        // when
        val moviesInfoMono = movieInfoRepository.findById("abc").log()

        // then
        StepVerifier.create(moviesInfoMono)
            .assertNext { assertEquals("Uncle", it.name) }
            .verifyComplete()
    }
}
