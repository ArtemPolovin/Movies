package com.example.data.repositories_impl

import com.example.data.cache.RequestTokenDataCache
import com.example.data.cache.SessionIdDataCache
import com.example.domain.repositories.CacheRepository

class CacheRepositoryImpl(
    private val sessionIdDataCache: SessionIdDataCache,
    private val requestTokenDataCache: RequestTokenDataCache
): CacheRepository {
    override fun deleteTokenFromCache() {
        sessionIdDataCache.removeSessionId()
        requestTokenDataCache.removeRequestToken()
    }
}