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

    fun getMovieInfoById(id: String): Mono<MovieInfo> {
        return movieInfoRepository.findById(id)
    }

    fun updateMovieInfo(updatedMovieInfo: MovieInfo, id: String): Mono<MovieInfo> {
        return movieInfoRepository.findById(id)
            .flatMap {
                it.cast = updatedMovieInfo.cast
                it.name = updatedMovieInfo.name
                it.releaseDate = updatedMovieInfo.releaseDate
                it.year = updatedMovieInfo.year

                movieInfoRepository.save(it)
            }
    }

    fun deleteMovieInfo(id: String): Mono<Void> {
        return movieInfoRepository.deleteById(id)
    }

    fun getMovieInfoByYear(year: Int): Flux<MovieInfo> {
        return movieInfoRepository.findByYear(year)
    }
}
