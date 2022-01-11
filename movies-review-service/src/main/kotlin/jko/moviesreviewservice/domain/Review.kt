package jko.moviesreviewservice.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

@Document
data class Review(

    @Id
    val reviewId: String?,

    @field:NotNull(message = "rating.movieInfoId: must not be null")
    val movieInfoId: Long?,

    var comment: String,

    @field:Min(value = 0L, message = "rating.negative: please pass a non-negative value")
    var rating: Double,
)
