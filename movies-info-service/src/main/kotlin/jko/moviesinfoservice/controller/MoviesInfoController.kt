package jko.moviesinfoservice.controller

import jko.moviesinfoservice.domain.MovieInfo
import jko.moviesinfoservice.service.MovieInfoService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
    fun getAllMovieInfos(@RequestParam(value = "year", required = false) year: Int?): Flux<MovieInfo> {
        if (year != null) {
            return movieInfoService.getMovieInfoByYear(year).log()
        }

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
    fun updateMovieInfo(
        @RequestBody updatedMovieInfo: MovieInfo,
        @PathVariable id: String
    ): Mono<ResponseEntity<MovieInfo>> {
        return movieInfoService.updateMovieInfo(updatedMovieInfo, id)
            .map { ResponseEntity.ok().body(it) }
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
            .log()
    }

    @DeleteMapping("/movieinfos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteMovieInfo(@PathVariable id: String): Mono<Void> {
        return movieInfoService.deleteMovieInfo(id).log()
    }
}
