package jko.moviesservice.controller

import jko.moviesservice.client.MoviesInfoRestClient
import jko.moviesservice.client.ReviewsRestClient
import jko.moviesservice.domain.Movie
import jko.moviesservice.domain.MovieInfo
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/v1/movies")
class MoviesController(
    private val moviesInfoRestClient: MoviesInfoRestClient,
    private val reviewsRestClient: ReviewsRestClient
) {

    @GetMapping("/{id}")
    fun retrieveByMovieId(@PathVariable("id") movieId: String): Mono<Movie> {
        return moviesInfoRestClient.retrieveMovieInfo(movieId)
            .flatMap { movieInfo ->
                val reviewsListMono = reviewsRestClient.retrieveReviews(movieId)
                    .collectList()

                reviewsListMono.map { reviews -> Movie(movieInfo, reviews) }
            }
    }

    @GetMapping("/stream", produces = [MediaType.APPLICATION_NDJSON_VALUE])
    fun retrieveMovieInfosStream(): Flux<MovieInfo> {
        return moviesInfoRestClient.retrieveMovieInfoStream()
    }
}
