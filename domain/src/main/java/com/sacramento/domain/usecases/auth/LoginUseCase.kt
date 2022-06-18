package com.sacramento.domain.usecases.auth

import com.sacramento.domain.models.LoginBodyModel
import com.sacramento.domain.repositories.AuthMovieRepository

class LoginUseCase(private val authMovieRepository: AuthMovieRepository) {
    suspend fun execute(loginBodyModel: LoginBodyModel) = authMovieRepository.login(loginBodyModel)
}