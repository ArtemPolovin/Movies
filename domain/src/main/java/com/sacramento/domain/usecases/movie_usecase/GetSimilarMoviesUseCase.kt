package com.sacramento.domain.usecases.movie_usecase

import com.sacramento.domain.repositories.MoviesRepository

class GetSimilarMoviesUseCase(private val movieRepository: MoviesRepository){
    suspend fun execute(movieId: Int) = movieRepository.getSimilarMovies(movieId)
}