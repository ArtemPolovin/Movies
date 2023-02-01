package com.sacramento.domain.repositories

interface CacheRepository {
    fun deleteTokenFromCache()
    fun saveSessionId(sessionId: String)
    fun loadSessionId(): String
    fun loadRequestToken(): String
    fun saveRequestToken(requestToken: String)
}