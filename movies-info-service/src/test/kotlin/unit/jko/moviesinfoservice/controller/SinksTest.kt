package jko.moviesinfoservice.controller

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks

class SinksTest {

    @Test
    fun sink_replay() {
        // given
        val replaySink: Sinks.Many<Int> = Sinks.many().replay().all()

        // when
        replaySink.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST)
        replaySink.emitNext(2, Sinks.EmitFailureHandler.FAIL_FAST)

        // then
        val integerFlux1: Flux<Int> = replaySink.asFlux()
        integerFlux1.subscribe {
            println("Subscriber 1: $it")
        }

        val integerFlux2: Flux<Int> = replaySink.asFlux()
        integerFlux2.subscribe {
            println("Subscriber 2: $it")
        }

        replaySink.tryEmitNext(3)
    }

    @Test
    fun sinks_multicast() {
        // given
        val multiCast: Sinks.Many<Int> = Sinks.many().multicast().onBackpressureBuffer()

        // when
        multiCast.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST)
        multiCast.emitNext(2, Sinks.EmitFailureHandler.FAIL_FAST)

        // then
        val integerFlux1: Flux<Int> = multiCast.asFlux()
        integerFlux1.subscribe {
            println("Subscriber 1: $it")
        }

        val integerFlux2: Flux<Int> = multiCast.asFlux()
        integerFlux2.subscribe {
            println("Subscriber 2: $it")
        }

        multiCast.emitNext(3, Sinks.EmitFailureHandler.FAIL_FAST)
        multiCast.emitNext(4, Sinks.EmitFailureHandler.FAIL_FAST)
    }

    @Test
    fun sinks_unicast() {
        // given
        val unitCast: Sinks.Many<Int> = Sinks.many().unicast().onBackpressureBuffer()

        // when
        unitCast.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST)
        unitCast.emitNext(2, Sinks.EmitFailureHandler.FAIL_FAST)

        // then
        val integerFlux1: Flux<Int> = unitCast.asFlux()
        integerFlux1.subscribe {
            println("Subscriber 1: $it")
        }

        unitCast.emitNext(3, Sinks.EmitFailureHandler.FAIL_FAST)
    }
}
