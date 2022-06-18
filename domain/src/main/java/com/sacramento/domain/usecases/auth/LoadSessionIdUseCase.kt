package com.sacramento.domain.usecases.auth

import com.sacramento.domain.repositories.CacheRepository

class LoadSessionIdUseCase(private val cacheRepository: CacheRepository) {
    fun execute() = cacheRepository.loadSessionId()

}