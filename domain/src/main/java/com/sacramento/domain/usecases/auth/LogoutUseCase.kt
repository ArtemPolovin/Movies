package com.sacramento.domain.usecases.auth

import com.sacramento.domain.models.LogoutRequestBodyModel
import com.sacramento.domain.repositories.AuthMovieRepository
import com.sacramento.domain.repositories.CacheRepository
import com.sacramento.domain.repositories.CookieRepository

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