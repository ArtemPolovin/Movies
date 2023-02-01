package com.sacramento.domain.usecases.movie_usecase

import com.sacramento.domain.repositories.MovieDBRepository

class CleanSavedMoviesDbUseCase(private val movieDBRepository: MovieDBRepository){
    suspend fun execute() = movieDBRepository.clearSavedMoviesDB()
}
