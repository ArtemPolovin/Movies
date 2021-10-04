package com.example.data.cache

import android.content.SharedPreferences
import com.example.data.utils.SESSION_ID

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