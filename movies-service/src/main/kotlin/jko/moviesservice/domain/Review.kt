package jko.moviesservice.domain

import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

data class Review(

    val reviewId: String?,

    @field:NotNull(message = "rating.movieInfoId: must not be null")
    val movieInfoId: Long?,

    var comment: String,

    @field:Min(value = 0L, message = "rating.negative: please pass a non-negative value")
    var rating: Double,
)
