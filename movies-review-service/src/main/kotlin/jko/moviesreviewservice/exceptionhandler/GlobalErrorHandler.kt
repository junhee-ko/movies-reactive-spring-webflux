package jko.moviesreviewservice.exceptionhandler

import jko.moviesreviewservice.exception.ReviewDataException
import jko.moviesreviewservice.exception.ReviewNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class GlobalErrorHandler : ErrorWebExceptionHandler {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        logger.error("Exception message: {}", ex.message, ex)

        val bufferFactory: DataBufferFactory = exchange.response.bufferFactory()
        val errorMessage: DataBuffer = bufferFactory.wrap(ex.message!!.toByteArray())

        if (ex is ReviewDataException) {
            exchange.response.statusCode = HttpStatus.BAD_REQUEST

            return exchange.response.writeWith(Mono.just(errorMessage))
        }
        if (ex is ReviewNotFoundException) {
            exchange.response.statusCode = HttpStatus.NOT_FOUND

            return exchange.response.writeWith(Mono.just(errorMessage))
        }

        exchange.response.statusCode = HttpStatus.INTERNAL_SERVER_ERROR
        return exchange.response.writeWith(Mono.just(errorMessage))
    }
}
