package jko.moviesservice.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document
data class MovieInfo(

    @Id
    val movieInfoId: String?,

    var name: String,

    var year: Int,

    var cast: List<String>,

    var releaseDate: LocalDate
)
