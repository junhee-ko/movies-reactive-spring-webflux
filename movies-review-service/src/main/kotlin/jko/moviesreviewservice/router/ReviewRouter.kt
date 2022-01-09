package jko.moviesreviewservice.router

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions.route
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class ReviewRouter {

    @Bean
    fun reviewsRoute(): RouterFunction<ServerResponse> {
        return route()
            .GET("/v1/helloworld") { ServerResponse.ok().bodyValue("helloworld") }
            .build()
    }
}
