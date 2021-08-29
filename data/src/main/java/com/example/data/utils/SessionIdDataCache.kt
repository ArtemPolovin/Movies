package com.example.data.utils

import android.content.SharedPreferences

class SessionIdDataCache(
    private val sharedPref: SharedPreferences
) {

    fun saveSessionId(sessionId: String) {
        sharedPref.edit().putString(SESSION_ID,sessionId).commit()
    }

    fun loadSessionId(): String{
        return sharedPref.getString(SESSION_ID, "") ?: ""
    }
}