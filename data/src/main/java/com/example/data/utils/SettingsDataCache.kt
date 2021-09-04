package com.example.data.utils

import android.content.SharedPreferences

class SettingsDataCache(
    private val sharedPrefManager: SharedPreferences
) {

    fun getLanguage() = sharedPrefManager.getString(LANGUAGE_SETTINGS_KEY, DEFAULT_LANGUAGE_VALUE)

}