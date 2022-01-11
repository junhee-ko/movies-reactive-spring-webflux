package jko.moviesreviewservice.exception

data class ReviewDataException(
    override val message: String,
    val ex: Throwable? = null
) : RuntimeException(message, ex)
