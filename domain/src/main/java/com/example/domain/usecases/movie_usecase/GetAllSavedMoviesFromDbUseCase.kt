package com.example.domain.usecases.movie_usecase

import com.example.domain.repositories.MoviesRepository

class GetAllSavedMoviesFromDbUseCase(private val moviesRepository: MoviesRepository) {
    suspend operator fun invoke() =  moviesRepository.getMovieListFromDb()
}