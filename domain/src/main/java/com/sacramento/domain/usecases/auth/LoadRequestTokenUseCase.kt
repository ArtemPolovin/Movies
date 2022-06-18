package com.sacramento.domain.usecases.auth

import com.sacramento.domain.repositories.CacheRepository

class LoadRequestTokenUseCase(private val cacheRepository: CacheRepository) {
    fun execute() = cacheRepository.loadRequestToken()
}