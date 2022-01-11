package jko.moviesservice.client

import jko.moviesservice.domain.Review
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Flux

@Component
class ReviewsRestClient(
    private val webClient: WebClient,
    @Value("\${restClient.reviewsUrl}") private val reviewsUrl: String
) {

    fun retrieveReviews(movieId: String): Flux<Review> {
        val url = UriComponentsBuilder.fromHttpUrl(reviewsUrl)
            .queryParam("movieInfoId", movieId)
            .buildAndExpand().toUriString()

        return webClient
            .get()
            .uri(url)
            .retrieve()
            .bodyToFlux(Review::class.java)
            .log()
    }
}
