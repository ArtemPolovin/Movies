package com.example.domain.usecases.movie_usecase

import com.example.domain.repositories.MoviesRepository

class GetTrailerListUseCase(private val moviesRepository: MoviesRepository) {
    suspend fun execute(movieId:Int) = moviesRepository.getTrailersList(movieId)
}