package com.example.domain.usecases.auth

import com.example.domain.models.LogoutRequestBodyModel
import com.example.domain.repositories.AuthMovieRepository

class LogoutUseCase(private val authMovieRepository: AuthMovieRepository) {
    suspend fun execute(logoutRequestBody: LogoutRequestBodyModel) = authMovieRepository.logout(logoutRequestBody)
}