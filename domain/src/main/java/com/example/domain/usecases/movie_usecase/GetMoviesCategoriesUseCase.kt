package com.example.domain.usecases.movie_usecase

import com.example.domain.repositories.MovieCategoriesRepository

class GetMoviesCategoriesUseCase(private val movieCategoriesRepository: MovieCategoriesRepository) {
      fun execute() = movieCategoriesRepository.getCategoriesList()
}