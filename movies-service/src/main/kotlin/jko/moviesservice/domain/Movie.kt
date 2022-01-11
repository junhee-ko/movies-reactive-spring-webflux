package jko.moviesservice.domain

data class Movie(

    val movieInfo: MovieInfo,
    val reviews: List<Review>
)
