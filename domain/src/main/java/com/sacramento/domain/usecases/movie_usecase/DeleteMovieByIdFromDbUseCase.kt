package com.sacramento.domain.usecases.movie_usecase

import com.sacramento.domain.repositories.MovieDBRepository
import com.sacramento.domain.repositories.MoviesRepository

class DeleteMovieByIdFromDbUseCase(private val movieDBRepository: MovieDBRepository) {
    //suspend operator fun invoke(movieId: List<Int>) = moviesRepository.deleteMovieById(movieId)
}