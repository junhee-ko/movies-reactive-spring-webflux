package jko.moviesreviewservice.router

import jko.moviesreviewservice.handler.ReviewHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates.path
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions.route
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class ReviewRouter {

    @Bean
    fun reviewsRoute(reviewHandler: ReviewHandler): RouterFunction<ServerResponse> {
        return route()
            .nest(path("/v1/reviews")) { builder ->
                builder
                    .POST("") { request -> reviewHandler.addReview(request) }
                    .GET("") { request -> reviewHandler.getReviews(request) }
                    .PUT("/{id}") { request -> reviewHandler.updateReview(request) }
                    .DELETE("/{id}") { request -> reviewHandler.deleteReview(request) }
            }
            .GET("/v1/helloworld") { ServerResponse.ok().bodyValue("helloworld") }
            .build()
    }
}
