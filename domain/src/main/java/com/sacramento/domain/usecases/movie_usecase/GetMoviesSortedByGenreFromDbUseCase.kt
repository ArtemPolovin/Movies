package com.sacramento.domain.usecases.movie_usecase

import com.sacramento.domain.repositories.MovieDBRepository

class GetMoviesSortedByGenreFromDbUseCase(private val moviesDBRepository: MovieDBRepository) {
    suspend fun execute() = moviesDBRepository.getMoviesSortedByGenreFromDB()
}