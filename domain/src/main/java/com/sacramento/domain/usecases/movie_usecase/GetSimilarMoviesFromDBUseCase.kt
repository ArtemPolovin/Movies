package com.sacramento.domain.usecases.movie_usecase

import com.sacramento.domain.repositories.MovieDBRepository

class GetSimilarMoviesFromDBUseCase(private val movieDBRepository: MovieDBRepository) {
    suspend fun execute(movieId: Int) = movieDBRepository.getSimilarMovies(movieId)
}