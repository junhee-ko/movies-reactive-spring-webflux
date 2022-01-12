package jko.moviesservice.globalerrorhandler

import jko.moviesservice.exception.MovieInfoClientException
import org.jboss.logging.Logger
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalErrorHandler {

    private val logger = Logger.getLogger(this::class.java)


    @ExceptionHandler(MovieInfoClientException::class)
    fun handlerClientException(ex: MovieInfoClientException): ResponseEntity<String> {
        logger.error("Exception caught in handlerClientException, ${ex.message}", ex)

        return ResponseEntity
            .status(ex.statusCode)
            .body(ex.message)
    }
}
