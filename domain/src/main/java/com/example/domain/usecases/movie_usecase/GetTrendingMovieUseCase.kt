package com.example.domain.usecases.movie_usecase

import com.example.domain.repositories.MoviesRepository

class GetTrendingMovieUseCase(private val moviesRepository: MoviesRepository) {
    suspend fun execute() = moviesRepository.getTrendingMovie()
}