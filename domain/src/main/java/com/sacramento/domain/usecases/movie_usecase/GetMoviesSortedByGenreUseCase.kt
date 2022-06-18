package com.sacramento.domain.usecases.movie_usecase

import com.sacramento.domain.repositories.MoviesRepository

class GetMoviesSortedByGenreUseCase(private val moviesRepository: MoviesRepository) {
    suspend fun execute()=  moviesRepository.getMoviesSortedByGenre()
}