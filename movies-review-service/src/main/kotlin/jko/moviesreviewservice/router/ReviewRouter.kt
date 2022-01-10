package jko.moviesreviewservice.router

import jko.moviesreviewservice.handler.ReviewHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions.route
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class ReviewRouter {

    @Bean
    fun reviewsRoute(reviewHandler: ReviewHandler): RouterFunction<ServerResponse> {
        return route()
            .GET("/v1/helloworld") { ServerResponse.ok().bodyValue("helloworld") }
            .POST("/v1/reviews") { request -> reviewHandler.addReview(request) }
            .GET("/v1/reviews") { request -> reviewHandler.getReviews(request) }
            .build()
    }
}
