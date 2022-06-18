package com.sacramento.domain.usecases.movie_usecase

import com.sacramento.domain.repositories.MoviesRepository

class GetWatchListUseCase(private val movieRepository: MoviesRepository) {
    suspend fun execute(sessionId: String) = movieRepository.getWatchList(sessionId)
}