package com.sacramento.domain.usecases.movie_usecase

import com.sacramento.domain.repositories.MovieCategoriesRepository

class GetGenresUseCase(private val categoryRepo: MovieCategoriesRepository) {
   suspend fun execute() = categoryRepo.getGenres()
}