package jko.moviesservice.service

import jko.moviesservice.domain.MovieInfo
import jko.moviesservice.repository.MovieInfoRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class MovieInfoService(
    private val movieInfoRepository: MovieInfoRepository
) {

    fun addMovieInfo(movieInfo: MovieInfo): Mono<MovieInfo> {
        return movieInfoRepository.save(movieInfo)
    }

    fun getAllMovieInfos(): Flux<MovieInfo> {
        return movieInfoRepository.findAll()
    }
}
