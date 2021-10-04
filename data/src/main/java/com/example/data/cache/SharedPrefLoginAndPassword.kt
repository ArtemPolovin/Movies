package com.example.data.cache

import android.content.SharedPreferences
import com.example.data.utils.PASSWORD
import com.example.data.utils.USER_NAME

class SharedPrefLoginAndPassword(
    private val sharedPref: SharedPreferences
) {

    fun saveUserName(userName: String) {
        sharedPref.edit().putString(USER_NAME, userName).apply()
    }

    fun savePassword(password: String) {
        sharedPref.edit().putString(PASSWORD, password).apply()
    }

    fun loadUserName(): String {
       return sharedPref.getString(USER_NAME, "") ?: ""
    }

    fun loadPassword() : String{
       return sharedPref.getString(PASSWORD, "") ?: ""
    }

    fun clearUserName() {
        sharedPref.edit().remove(USER_NAME).apply()
    }

    fun clearPassword() {
        sharedPref.edit().remove(PASSWORD).apply()
    }
}