package jko.moviesinfoservice.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

@RestController
class FluxAndMonoController {

    @GetMapping("/flux")
    fun flux(): Flux<Int> = Flux.just(1, 2, 3).log()

    @GetMapping("/mono")
    fun mono(): Mono<String> = Mono.just("1").log()

    @GetMapping("/stream", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun stream(): Flux<Long> = Flux.interval(Duration.ofSeconds(1)).log()
}
