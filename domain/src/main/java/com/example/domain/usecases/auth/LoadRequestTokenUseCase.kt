package com.example.domain.usecases.auth

import com.example.domain.repositories.CacheRepository

class LoadRequestTokenUseCase(private val cacheRepository: CacheRepository) {
    fun execute() = cacheRepository.loadRequestToken()
}