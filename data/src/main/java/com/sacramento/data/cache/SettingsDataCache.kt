package com.sacramento.data.cache

import android.content.SharedPreferences
import com.sacramento.data.utils.DEFAULT_ENGLISH_LANGUAGE_VALUE
import com.sacramento.data.utils.LANGUAGE_SETTINGS_KEY

class SettingsDataCache(
    private val sharedPrefManager: SharedPreferences
) {

    fun getLanguage() = sharedPrefManager.getString(LANGUAGE_SETTINGS_KEY, DEFAULT_ENGLISH_LANGUAGE_VALUE)

}