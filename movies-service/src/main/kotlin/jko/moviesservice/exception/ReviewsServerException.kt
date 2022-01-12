package jko.moviesservice.exception

data class ReviewsServerException(
    override val message: String,
    val statusCode: Int
) : RuntimeException(message)
