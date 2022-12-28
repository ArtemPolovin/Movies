package com.sacramento.domain.usecases.movie_usecase

import com.sacramento.domain.repositories.MovieDBRepository

class GetMovieByIdFromDBUseCase(private val movieDBRepository: MovieDBRepository) {
    suspend fun execute(movieId: Int) = movieDBRepository.getMovieById(movieId)
}