package jko.moviesservice.repository

import jko.moviesservice.domain.MovieInfo
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface MovieInfoRepository : ReactiveMongoRepository<MovieInfo, String>
