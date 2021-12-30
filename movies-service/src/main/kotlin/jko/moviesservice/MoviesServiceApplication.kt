package jko.moviesservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MoviesServiceApplication

fun main(args: Array<String>) {
    runApplication<MoviesServiceApplication>(*args)
}
