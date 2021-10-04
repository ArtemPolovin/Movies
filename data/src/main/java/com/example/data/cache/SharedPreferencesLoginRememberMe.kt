package com.example.data.cache

import android.content.SharedPreferences
import com.example.data.utils.REMEMBER_ME

class SharedPreferencesLoginRememberMe(
    private val sharedPref: SharedPreferences
) {

    fun saveIsRememberMeChecked(isRememberMeChecked: Boolean) {
        sharedPref.edit().putBoolean(REMEMBER_ME,isRememberMeChecked).apply()
    }

    fun loadIsRememberMeChecked() =
        sharedPref.getBoolean(REMEMBER_ME, false)

}