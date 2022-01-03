package jko.moviesservice.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document
data class MovieInfo(

    @Id
    val movieInfoId: String?,

    val name: String,

    var year: Int,

    val cast: List<String>,

    val releaseDate: LocalDate
)
