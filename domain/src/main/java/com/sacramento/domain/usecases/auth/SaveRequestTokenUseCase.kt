package com.sacramento.domain.usecases.auth

import com.sacramento.domain.repositories.AuthMovieRepository

class SaveRequestTokenUseCase(
    private val authMovieRepository: AuthMovieRepository,
) {
    suspend fun execute() = authMovieRepository.saveRequestToken()
}