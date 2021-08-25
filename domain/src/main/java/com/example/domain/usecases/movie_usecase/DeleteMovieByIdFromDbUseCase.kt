package com.example.domain.usecases.movie_usecase

import com.example.domain.repositories.MoviesRepository

class DeleteMovieByIdFromDbUseCase(private val moviesRepository: MoviesRepository) {
    suspend operator fun invoke(movieId: List<Int>) = moviesRepository.deleteMovieById(movieId)
}