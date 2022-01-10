package jko.moviesreviewservice.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Review(

    @Id
    val reviewId: String?,

    val movieInfoId: Long,

    var comment: String,

    var rating: Double,
)
