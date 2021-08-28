package com.example.movies.utils

import android.content.SharedPreferences

class SharedPreferencesLogin(
    private val sharedPref: SharedPreferences
) {

    fun saveIsRememberMeChecked(isRememberMeChecked: Boolean) {
        sharedPref.edit().putBoolean(REMEMBER_ME,isRememberMeChecked).apply()
    }

    fun loadIsRememberMeChecked() {
        sharedPref.getBoolean(REMEMBER_ME, false)
    }
}