package jko.moviesservice.exception

data class ReviewsClientException(
    override val message: String,
    val statusCode: Int
) : RuntimeException(message)
