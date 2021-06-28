package com.example.domain.usecases.movie_usecase

import com.example.domain.models.PopularMovieWithDetailsModel
import com.example.domain.repositories.MoviesRepository

class InsertMovieToDbUseCase(private val moviesRepository: MoviesRepository) {
    suspend operator fun invoke(movie: PopularMovieWithDetailsModel) =
        moviesRepository.saveMovieToEntity(movie)
}