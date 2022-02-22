package com.example.domain.usecases.movie_usecase

import com.example.domain.repositories.MoviesRepository

class GetWatchListUseCase(private val movieRepository: MoviesRepository) {
    suspend fun execute(sessionId: String) = movieRepository.getWatchList(sessionId)
}