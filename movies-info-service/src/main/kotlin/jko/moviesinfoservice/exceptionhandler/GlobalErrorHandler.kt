package jko.moviesinfoservice.exceptionhandler

import org.slf4j.LoggerFactory
import org.springframework.context.support.DefaultMessageSourceResolvable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.support.WebExchangeBindException
import java.util.stream.Collectors

@ControllerAdvice
class GlobalErrorHandler {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(value = [WebExchangeBindException::class])
    fun handleRequestBodyError(ex: WebExchangeBindException): ResponseEntity<String> {
        logger.error("Exception Caught in handleRequestBodyError: {}", ex.message, ex)

        val error = ex.bindingResult.allErrors.stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .sorted()
            .collect(Collectors.joining(","))

        logger.error("Error is: {}", error)

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }
}
