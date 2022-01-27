package com.example.domain.usecases.movie_usecase

import com.example.domain.repositories.MoviesRepository

class GetRecommendationsMoviesUseCase(private val movieRepository: MoviesRepository) {
    suspend fun execute(movieId: Int) = movieRepository.getRecommendationsMovies(movieId)
}