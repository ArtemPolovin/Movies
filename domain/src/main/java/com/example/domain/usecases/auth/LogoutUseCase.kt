package com.example.domain.usecases.auth

import com.example.domain.models.LogoutRequestBodyModel
import com.example.domain.repositories.AuthMovieRepository
import com.example.domain.repositories.CacheRepository
import com.example.domain.repositories.CookieRepository

class LogoutUseCase(
    private val authMovieRepository: AuthMovieRepository,
    private val cookieRepository: CookieRepository,
    private val cacheRepository: CacheRepository
) {
    suspend fun execute(logoutRequestBody: LogoutRequestBodyModel): Boolean {
        val isLoggedOutSuccess = authMovieRepository.logout(logoutRequestBody)

        return if (isLoggedOutSuccess) {
            cookieRepository.clearCookies()
            cacheRepository.deleteTokenFromCache()
            true
        } else {
            false
        }
    }
}