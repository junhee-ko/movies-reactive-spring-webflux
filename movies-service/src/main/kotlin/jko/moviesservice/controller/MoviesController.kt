package jko.moviesservice.controller

import jko.moviesservice.domain.Movie
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/v1/movies")
class MoviesController {

    @GetMapping("/{id}")
    fun retrieveByMovieId(@PathVariable("id") movieId: String): Mono<Movie>? {

        return null
    }
}
