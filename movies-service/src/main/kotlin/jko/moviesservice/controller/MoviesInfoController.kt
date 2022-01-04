package jko.moviesservice.controller

import jko.moviesservice.domain.MovieInfo
import jko.moviesservice.service.MovieInfoService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import javax.validation.Valid

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
    fun addMovieInfo(@RequestBody @Valid movieInfo: MovieInfo): Mono<MovieInfo> {
        return movieInfoService.addMovieInfo(movieInfo).log()
    }

    @PutMapping("/movieinfos/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateMovieInfo(@RequestBody updatedMovieInfo: MovieInfo, @PathVariable id: String): Mono<MovieInfo> {
        return movieInfoService.updateMovieInfo(updatedMovieInfo, id).log()
    }

    @DeleteMapping("/movieinfos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteMovieInfo(@PathVariable id: String): Mono<Void> {
        return movieInfoService.deleteMovieInfo(id).log()
    }
}
