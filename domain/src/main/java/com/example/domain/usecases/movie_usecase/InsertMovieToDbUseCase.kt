package com.example.domain.usecases.movie_usecase

import com.example.domain.models.MovieWithDetailsModel
import com.example.domain.repositories.MoviesRepository

class InsertMovieToDbUseCase(private val moviesRepository: MoviesRepository) {
    suspend operator fun invoke(movie: MovieWithDetailsModel) =
        moviesRepository.saveMovieToEntity(movie)
}