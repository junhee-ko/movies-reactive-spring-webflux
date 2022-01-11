package jko.moviesinfoservice.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

@Document
data class MovieInfo(

    @Id
    val movieInfoId: String?,

    @field:NotBlank(message = "movieInfo.name must be present")
    var name: String,

    @field:NotNull
    @field:Positive(message = "movieInfo.year must be positive value")
    var year: Int,


    var cast: List<@NotBlank(message = "movieInfo.cast be present") String>, // not work in kotlin

    var releaseDate: LocalDate
)
