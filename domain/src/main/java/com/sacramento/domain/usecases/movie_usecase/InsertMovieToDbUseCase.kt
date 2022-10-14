package com.sacramento.domain.usecases.movie_usecase

import com.sacramento.domain.models.MovieWithDetailsModel
import com.sacramento.domain.repositories.MovieDBRepository
import com.sacramento.domain.repositories.MoviesRepository

class InsertMovieToDbUseCase(private val movieDBRepository: MovieDBRepository) {
//    suspend operator fun invoke(movie: MovieWithDetailsModel) =
//        moviesRepository.saveMovieToEntity(movie)
}