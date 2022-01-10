package jko.moviesreviewservice.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Review(

    @Id
    val reviewId: String?,

    val movieInfoId: Long,

    val comment: String,

    val rating: Double,
)
