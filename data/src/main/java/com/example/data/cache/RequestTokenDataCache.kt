package com.example.data.cache

import android.content.SharedPreferences
import com.example.data.utils.REQUEST_TOKEN

class RequestTokenDataCache(
    private val sharedPref: SharedPreferences
) {

    fun removeRequestToken() {
        sharedPref.edit().clear().apply()
    }

    fun loadRequestToken(): String =
        sharedPref.getString(REQUEST_TOKEN, "" ) ?: ""

    fun saveRequestToken(requestToken: String) {
        sharedPref.edit().putString(REQUEST_TOKEN, requestToken).commit()
    }
}