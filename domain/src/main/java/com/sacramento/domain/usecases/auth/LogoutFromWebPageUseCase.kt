package com.sacramento.domain.usecases.auth

import com.sacramento.domain.repositories.CacheRepository
import com.sacramento.domain.repositories.CookieRepository

class LogoutFromWebPageUseCase(
    private val cookieRepository: CookieRepository,
    private val cacheRepository: CacheRepository
) {
    fun execute(){
        cookieRepository.clearCookies()
        cacheRepository.deleteTokenFromCache()
    }
}