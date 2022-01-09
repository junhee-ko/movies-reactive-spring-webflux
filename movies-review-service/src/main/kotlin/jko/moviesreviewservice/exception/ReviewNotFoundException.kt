package jko.moviesreviewservice.exception

data class ReviewNotFoundException(
    override val message: String,
    val ex: Throwable
) : RuntimeException(message, ex)
