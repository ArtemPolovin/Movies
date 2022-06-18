package com.sacramento.domain.usecases.auth

import com.sacramento.domain.models.SessionIdRequestBodyModel
import com.sacramento.domain.repositories.AuthMovieRepository

class SaveSessionIdUseCase(private val authRepository: AuthMovieRepository) {

    suspend fun save(sessionIdModel: SessionIdRequestBodyModel) =
        authRepository.saveSessionId(sessionIdModel)
}