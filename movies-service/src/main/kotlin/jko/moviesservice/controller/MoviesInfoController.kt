package jko.moviesservice.controller

import jko.moviesservice.domain.MovieInfo
import jko.moviesservice.service.MovieInfoService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/v1")
class MoviesInfoController(
    private val movieInfoService: MovieInfoService
) {


    @GetMapping("/movieinfos")
    fun getAllMovieInfos(): Flux<MovieInfo> {
        return movieInfoService.getAllMovieInfos().log()
    }

    @GetMapping("/movieinfos/{id}")
    fun getMovieInfoById(@PathVariable id: String): Mono<MovieInfo> {
        return movieInfoService.getMovieInfoById(id).log()
    }

    @PostMapping("/movieinfos")
    @ResponseStatus(HttpStatus.CREATED)
    fun addMovieInfo(@RequestBody movieInfo: MovieInfo): Mono<MovieInfo> {
        return movieInfoService.addMovieInfo(movieInfo).log()
    }
}
