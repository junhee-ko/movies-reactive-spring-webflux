package jko.moviesservice.controller

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.test.StepVerifier

@WebFluxTest(controllers = [FluxAndMonoController::class])
@AutoConfigureWebTestClient
internal class FluxAndMonoControllerTest(
    @Autowired private val webTestClient: WebTestClient
) {

    @Test
    fun flux_1() {
        webTestClient.get()
            .uri("/flux")
            .exchange()
            .expectStatus()
            .is2xxSuccessful
            .expectBodyList(Int::class.java)
            .hasSize(3)
    }

    @Test
    fun flux_2() {
        val flux = webTestClient.get()
            .uri("/flux")
            .exchange()
            .expectStatus()
            .is2xxSuccessful
            .returnResult(Int::class.java)
            .responseBody

        StepVerifier.create(flux)
            .expectNext(1, 2, 3)
            .verifyComplete()
    }

    @Test
    fun flux_3() {
        webTestClient.get()
            .uri("/flux")
            .exchange()
            .expectStatus()
            .is2xxSuccessful
            .expectBodyList(Int::class.java)
            .consumeWith<WebTestClient.ListBodySpec<Int>> {
                assert(it.responseBody?.size == 3)
            }
    }

    @Test
    fun mono() {
        webTestClient.get()
            .uri("/mono")
            .exchange()
            .expectStatus()
            .is2xxSuccessful
            .expectBody(String::class.java)
            .consumeWith {
                assertEquals("1", it.responseBody)
            }
    }
}
