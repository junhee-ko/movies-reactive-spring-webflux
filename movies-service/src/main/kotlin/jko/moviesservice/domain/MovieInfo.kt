package jko.moviesservice.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document
class MovieInfo(

    @Id
    private val movieInfoId: String,

    private val name: String,

    private val year: Int,

    private val cast: List<String>,

    private val releaseDate: LocalDate
)
