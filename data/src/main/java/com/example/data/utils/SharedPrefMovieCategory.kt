package com.example.data.utils

import android.content.SharedPreferences

class SharedPrefMovieCategory(
    private val sharedPref: SharedPreferences
) {

    fun saveMovieCategory(movieCategory: String) {
        sharedPref.edit().putString(MOVIE_CATEGORY,movieCategory).apply()
    }

    fun loadMovieCategory() =
        sharedPref.getString(MOVIE_CATEGORY,"")?: ""
}