package com.example.data.repositories_impl

import android.content.SharedPreferences
import com.example.data.utils.REQUEST_TOKEN
import com.example.data.utils.SESSION_ID
import com.example.domain.repositories.CacheRepository

class CacheRepositoryImpl(
    private val sharedPref: SharedPreferences
) : CacheRepository {

    override fun deleteTokenFromCache() {
        removeSessionId()
        removeRequestToken()
    }

    override fun saveSessionId(sessionId: String) {
        sharedPref.edit().putString(SESSION_ID, sessionId).commit()
    }

    override fun loadSessionId(): String {
        return sharedPref.getString(SESSION_ID, "") ?: ""
    }

    private fun removeSessionId() {
        sharedPref.edit().clear().apply()
    }

    private fun removeRequestToken() {
        sharedPref.edit().clear().apply()
    }

    override fun loadRequestToken(): String =
        sharedPref.getString(REQUEST_TOKEN, "") ?: ""

    override fun saveRequestToken(requestToken: String) {
        sharedPref.edit().putString(REQUEST_TOKEN, requestToken).commit()
    }
}