package com.example.domain.usecases.auth

import com.example.domain.models.LoginBodyModel
import com.example.domain.models.SessionIdRequestBodyModel
import com.example.domain.repositories.AuthMovieRepository

class LoginUseCase(private val authMovieRepository: AuthMovieRepository) {
    suspend fun execute(loginBodyModel: LoginBodyModel) = authMovieRepository.login(loginBodyModel)
}