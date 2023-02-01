package com.sacramento.domain.usecases.movie_usecase

import com.sacramento.domain.repositories.MoviesRepository

class GetMoviePosterUseCase(private val moviesRepository: MoviesRepository) {
    suspend fun execute(url: String) = moviesRepository.getMoviePoster(url)
}