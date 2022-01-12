package jko.moviesservice.client

import jko.moviesservice.domain.MovieInfo
import jko.moviesservice.exception.MovieInfoClientException
import jko.moviesservice.exception.MovieInfoServerException
import org.jboss.logging.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class MoviesInfoRestClient(
    private val webClient: WebClient,
    @Value("\${restClient.moviesInfoUrl}") private val movieInfoUrl: String
) {
    private val logger = Logger.getLogger(this::class.java)

    fun retrieveMovieInfo(movieId: String): Mono<MovieInfo> {
        val url = "$movieInfoUrl/{id}"

        return webClient
            .get()
            .uri(url, movieId)
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError) { clientResponse: ClientResponse ->
                logger.info("Status code: ${clientResponse.statusCode().value()}")

                if (clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
                    Mono.error(
                        MovieInfoClientException(
                            "there is no movieInfo available in id: $movieId",
                            clientResponse.statusCode().value()
                        )
                    )
                } else {
                    clientResponse.bodyToMono(String::class.java)
                        .flatMap { response ->
                            Mono.error(
                                MovieInfoClientException(
                                    response,
                                    clientResponse.statusCode().value()
                                )
                            )
                        }
                }
            }
            // server 가 만약 완전히 down 되면 이 에러를 받지 못함
            .onStatus(HttpStatus::is5xxServerError) { clientResponse: ClientResponse ->
                logger.info("Status code: ${clientResponse.statusCode().value()}")

                Mono.error(
                    MovieInfoServerException(
                        "Server Exception in MoviesInfoService", clientResponse.statusCode().value()
                    )
                )
            }
            .bodyToMono(MovieInfo::class.java)
            .log()
    }
}
