package com.sacramento.domain.usecases.movie_usecase

import com.sacramento.domain.repositories.MoviesRepository

class GetRecommendationsMoviesUseCase(private val movieRepository: MoviesRepository) {
    suspend fun execute(movieId: Int) = movieRepository.getRecommendationsMovies(movieId)
}