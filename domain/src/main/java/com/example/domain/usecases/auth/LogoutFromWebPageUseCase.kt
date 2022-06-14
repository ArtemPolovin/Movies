package com.example.domain.usecases.auth

import com.example.domain.repositories.CacheRepository
import com.example.domain.repositories.CookieRepository

class LogoutFromWebPageUseCase(
    private val cookieRepository: CookieRepository,
    private val cacheRepository: CacheRepository
) {
    fun execute(){
        cookieRepository.clearCookies()
        cacheRepository.deleteTokenFromCache()
    }
}