package jko.moviesservice.exception

data class MovieInfoServerException(
    override val message: String,
    val statusCode: Int
) : RuntimeException(message)
