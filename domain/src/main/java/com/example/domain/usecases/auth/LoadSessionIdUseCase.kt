package com.example.domain.usecases.auth

import com.example.domain.repositories.CacheRepository

class LoadSessionIdUseCase(private val cacheRepository: CacheRepository) {
    fun execute() = cacheRepository.loadSessionId()

}