package com.sacramento.domain.usecases.movie_usecase

import com.sacramento.domain.repositories.MoviesRepository

class GetAllSavedMoviesFromDbUseCase(private val moviesRepository: MoviesRepository) {
    suspend operator fun invoke() =  moviesRepository.getMovieListFromDb()
}