package com.example.domain.usecases.auth

import com.example.domain.models.SessionIdRequestBodyModel
import com.example.domain.repositories.AuthMovieRepository

class SaveSessionIdUseCase(private val authRepository: AuthMovieRepository) {

    suspend fun save(sessionIdModel: SessionIdRequestBodyModel) =
        authRepository.saveSessionId(sessionIdModel)
}