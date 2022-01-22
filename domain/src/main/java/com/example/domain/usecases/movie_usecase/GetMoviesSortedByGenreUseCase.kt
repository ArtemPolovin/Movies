package com.example.domain.usecases.movie_usecase

import com.example.domain.repositories.MoviesRepository

class GetMoviesSortedByGenreUseCase(private val moviesRepository: MoviesRepository) {
    suspend fun execute()=  moviesRepository.getMoviesSortedByGenre()
}