package com.sacramento.domain.usecases.movie_usecase

import com.sacramento.domain.models.MovieWithDetailsModel
import com.sacramento.domain.repositories.MoviesRepository

class InsertMovieToDbUseCase(private val moviesRepository: MoviesRepository) {
    suspend operator fun invoke(movie: MovieWithDetailsModel) =
        moviesRepository.saveMovieToEntity(movie)
}