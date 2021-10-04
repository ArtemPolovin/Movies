package com.example.data.cache

import android.content.SharedPreferences
import com.example.data.utils.DEFAULT_LANGUAGE_VALUE
import com.example.data.utils.LANGUAGE_SETTINGS_KEY

class SettingsDataCache(
    private val sharedPrefManager: SharedPreferences
) {

    fun getLanguage() = sharedPrefManager.getString(LANGUAGE_SETTINGS_KEY, DEFAULT_LANGUAGE_VALUE)

}