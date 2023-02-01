package com.sacramento.domain.usecases.movie_usecase

import com.sacramento.domain.repositories.MovieCategoriesRepository

class GetMoviesCategoriesUseCase(private val movieCategoriesRepository: MovieCategoriesRepository) {
     suspend fun execute() = movieCategoriesRepository.getCategoriesList()
}