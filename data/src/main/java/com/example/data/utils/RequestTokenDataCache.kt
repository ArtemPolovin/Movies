package com.example.data.utils

import android.content.SharedPreferences

class RequestTokenDataCache(
    private val sharedPref: SharedPreferences
) {

    fun loadRequestToken(): String =
        sharedPref.getString(REQUEST_TOKEN, "" ) ?: ""

    fun saveRequestToken(requestToken: String) {
        sharedPref.edit().putString(REQUEST_TOKEN, requestToken).apply()
    }
}