package com.example.data.cache

import android.content.SharedPreferences
import com.example.data.utils.REMEMBER_ME
import com.example.data.utils.WATCH_LIST

class WatchListChanges(private val sharedPref: SharedPreferences) {

    fun saveIsWatchListChanged(isWatchListChanged: Boolean) {
        sharedPref.edit().putBoolean(WATCH_LIST,isWatchListChanged).apply()
    }

    fun loadIsWatchListChanged() =
        sharedPref.getBoolean(WATCH_LIST, false)
}