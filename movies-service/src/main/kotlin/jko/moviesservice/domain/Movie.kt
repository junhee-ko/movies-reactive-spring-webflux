package jko.moviesservice.domain

data class Movie(

    private val movieInfo: MovieInfo,
    val reviews: List<Review>
)
