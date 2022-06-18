package com.sacramento.domain.usecases.movie_usecase

import com.sacramento.domain.repositories.MoviesRepository

class GetTrendingMovieUseCase(private val moviesRepository: MoviesRepository) {
    suspend fun execute() = moviesRepository.getTrendingMovie()
}