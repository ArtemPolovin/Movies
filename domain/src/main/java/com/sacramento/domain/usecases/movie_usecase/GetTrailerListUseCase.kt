package com.sacramento.domain.usecases.movie_usecase

import com.sacramento.domain.repositories.MoviesRepository

class GetTrailerListUseCase(private val moviesRepository: MoviesRepository) {
    suspend fun execute(movieId:Int) = moviesRepository.getTrailersList(movieId)
}