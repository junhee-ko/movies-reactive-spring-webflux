package jko.moviesservice.exception

data class MovieInfoClientException(
    override val message: String,
    val statusCode: Int
) : RuntimeException(message)
