package com.sacramento.domain.usecases.movie_usecase

import com.sacramento.domain.models.MovieModel
import com.sacramento.domain.repositories.MovieDBRepository

class InsertAllSavedMoviesFromAccountToDBUseCase(private val movieDBRepository: MovieDBRepository) {
    suspend fun execute(savedMovieList: List<MovieModel>) =
        movieDBRepository.insertAllSavedMoviesFromAccount(savedMovieList)
}