package com.example.domain.usecases.movie_usecase

import com.example.domain.repositories.MoviesRepository

class GetMovieDetailsUseCase(private val moviesRepository: MoviesRepository){
    suspend fun execute(movieId: Int) = moviesRepository.getMovieDetailsForDetailsPage(movieId)
}