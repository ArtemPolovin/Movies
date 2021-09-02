package com.example.domain.usecases.movie_usecase

import com.example.domain.repositories.BackendLessRepository

class GetMoviesCategoriesCellsUseCase(private val backendLessRepository: BackendLessRepository) {
    suspend operator fun invoke() = backendLessRepository.getMovieCategoriesImages()
}