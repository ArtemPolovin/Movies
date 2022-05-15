package com.example.domain.usecases.movie_usecase

import com.example.domain.repositories.MoviesRepository

class GetMoviePosterUseCase(private val moviesRepository: MoviesRepository) {
    suspend fun execute(url: String) = moviesRepository.getMoviePoster(url)
}