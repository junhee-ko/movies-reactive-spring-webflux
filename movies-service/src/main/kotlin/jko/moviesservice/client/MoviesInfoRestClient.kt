package jko.moviesservice.client

import jko.moviesservice.domain.MovieInfo
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class MoviesInfoRestClient(
    private val webClient: WebClient,
    @Value("\${restClient.moviesInfoUrl}") private val movieInfoUrl: String
) {


    fun retrieveMovieInfo(movieId: String): Mono<MovieInfo> {
        val url = "$movieInfoUrl/{id}"

        return webClient
            .get()
            .uri(url, movieId)
            .retrieve()
            .bodyToMono(MovieInfo::class.java)
            .log()
    }
}
