package jko.moviesinfoservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MoviesInfoServiceApplication

fun main(args: Array<String>) {
    runApplication<MoviesInfoServiceApplication>(*args)
}
