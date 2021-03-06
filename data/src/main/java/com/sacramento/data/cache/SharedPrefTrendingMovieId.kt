package com.sacramento.data.cache

import android.content.SharedPreferences
import com.sacramento.data.utils.TRENDING_MOVIE_ID

class SharedPrefTrendingMovieId(private val sharedPref: SharedPreferences) {

    fun saveTrendingMovieId(movieId: Int) {
        sharedPref.edit().putInt(TRENDING_MOVIE_ID,movieId).apply()
    }

    fun loadTrendingMovieId() = sharedPref.getInt(TRENDING_MOVIE_ID,0)
}