package com.example.data.cache

import android.content.SharedPreferences
import com.example.data.utils.GENRE_ID
import com.example.data.utils.MOVIE_CATEGORY

class SharedPrefMovieCategory(
    private val sharedPref: SharedPreferences
) {

    fun saveMovieCategory(movieCategory: String) {
        sharedPref.edit().putString(MOVIE_CATEGORY,movieCategory).apply()
    }

    fun loadMovieCategory() =
        sharedPref.getString(MOVIE_CATEGORY,"Popular")

    fun clearMovieCategory() {
        sharedPref.edit().remove(MOVIE_CATEGORY).apply()
    }

    fun saveGenreId(genreId: String) {
        sharedPref.edit().putString(GENRE_ID, genreId).apply()
    }

    fun loadGenreId() = sharedPref.getString(GENRE_ID,"53")
}