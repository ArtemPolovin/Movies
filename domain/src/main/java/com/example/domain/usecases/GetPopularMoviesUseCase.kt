package com.example.domain.usecases

import com.example.domain.repositories.MoviesRepository

class GetPopularMoviesUseCase(private val moviesRepository: MoviesRepository) {
    operator fun invoke() =  moviesRepository.getPopularMovies()
}