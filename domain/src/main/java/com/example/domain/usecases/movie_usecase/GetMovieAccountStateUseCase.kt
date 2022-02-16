package com.example.domain.usecases.movie_usecase

import com.example.domain.repositories.MoviesRepository

class GetMovieAccountStateUseCase(private val movieRepository: MoviesRepository) {
    suspend fun execute(sessionId: String, movieId: Int) = movieRepository.getMovieAccountState(sessionId,movieId)
}