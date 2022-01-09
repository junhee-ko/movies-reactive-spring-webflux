package jko.moviesreviewservice.repository

import jko.moviesreviewservice.domain.Review
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface ReviewReactiveRepository : ReactiveMongoRepository<Review, String>
