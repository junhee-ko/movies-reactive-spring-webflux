package jko.moviesservice.client

import jko.moviesservice.domain.Review
import jko.moviesservice.exception.ReviewsClientException
import jko.moviesservice.exception.ReviewsServerException
import jko.moviesservice.util.RetryUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class ReviewsRestClient(
    private val webClient: WebClient,
    @Value("\${restClient.reviewsUrl}") private val reviewsUrl: String
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun retrieveReviews(movieId: String): Flux<Review> {
        val url = UriComponentsBuilder.fromHttpUrl(reviewsUrl)
            .queryParam("movieInfoId", movieId)
            .buildAndExpand().toUriString()

        return webClient
            .get()
            .uri(url)
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError) { clientResponse: ClientResponse ->
                logger.info("Status code: ${clientResponse.statusCode().value()}")

                if (clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
                    Mono.empty()
                } else {
                    clientResponse.bodyToMono(String::class.java)
                        .flatMap { response ->
                            Mono.error(
                                ReviewsClientException(
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
                    ReviewsServerException(
                        "Server Exception in ReviewsService", clientResponse.statusCode().value()
                    )
                )
            }
            .bodyToFlux(Review::class.java)
            .retryWhen(RetryUtil.retrySpec())
            .log()
    }
}
