package com.example.domain.usecases.movie_usecase

import com.example.domain.repositories.MovieCategoriesRepository

class GetGenresUseCase(private val categoryRepo: MovieCategoriesRepository) {
    fun execute() = categoryRepo.getGenres()
}