package com.example.domain.usecases.auth

import com.example.domain.repositories.AuthMovieRepository

class SaveRequestTokenUseCase(
    private val authMovieRepository: AuthMovieRepository,
) {
    suspend fun execute() = authMovieRepository.saveRequestToken()
}