package com.sacramento.domain.usecases.movie_usecase

import com.sacramento.domain.repositories.MoviesRepository

class GetMovieDetailsUseCase(private val moviesRepository: MoviesRepository){
    suspend fun execute(movieId: Int) = moviesRepository.getMovieDetailsForDetailsPage(movieId)
}