package com.example.data.cache

import android.content.SharedPreferences
import com.example.data.utils.*

class SharedPrefMovieFilter(private val sharPref: SharedPreferences) {

    fun clearFilterCache() {
        sharPref.edit().clear().apply()
    }

    fun saveRating(rating: Int) {
        sharPref.edit().putInt(RATING, rating).apply()
    }

    fun loadRating() = sharPref.getInt(RATING, 0)

    fun clearRating() {
        sharPref.edit().remove(RATING).apply()
    }

    fun saveRatingCheckBoxState(isChecked: Boolean) {
        sharPref.edit().putBoolean(RATING_IS_CHECKED, isChecked).apply()
    }

    fun loadRatingCheckboxState() = sharPref.getBoolean(RATING_IS_CHECKED, false)

    fun saveRatingPosition(position: Int) {
        sharPref.edit().putInt(RATING_POSITION, position).apply()
    }

    fun loadRatingPosition() = sharPref.getInt(RATING_POSITION, 0)

    fun clearRatingPosition() {
        sharPref.edit().remove(RATING_POSITION).apply()
    }

    fun saveReleaseYear(year: String) {
        sharPref.edit().putString(RELEASE_YEAR, year).apply()
    }

    fun loadReleaseYear() = sharPref.getString(RELEASE_YEAR, "")

    fun clearReleaseYar() {
        sharPref.edit().remove(RELEASE_YEAR).apply()
    }

    fun saveReleaseYearCheckBoxState(isChecked: Boolean) {
        sharPref.edit().putBoolean(RELEASE_YEAR_IS_CHECKED, isChecked).apply()
    }

    fun loadReleaseYearCheckBoxState() = sharPref.getBoolean(RELEASE_YEAR_IS_CHECKED, false)

    fun saveGenreCheckBoxState(isChecked: Boolean) {
        sharPref.edit().putBoolean(GENRE_IS_CHECKED, isChecked).apply()
    }

    fun loadGenreCheckBoxState() = sharPref.getBoolean(GENRE_IS_CHECKED, false)

    fun saveGenreSpinnerPosition(position: Int) {
        sharPref.edit().putInt(GENRE_SPINNER_POSITION, position).apply()
    }

    fun loadGenreSpinnerPosition() = sharPref.getInt(GENRE_SPINNER_POSITION, 0)

    fun clearGenreSpinnerPosition() {
        sharPref.edit().remove(GENRE_SPINNER_POSITION).apply()
    }

    fun saveSortByPopularity() {
        sharPref.edit().putString(SORT_BY_POPULARITY, POPULARITY_DATA).apply()
    }

    fun loadSortByPopularity() = sharPref.getString(SORT_BY_POPULARITY, "")

    fun saveSortByPopularityCheckBoxState(isChecked: Boolean) {
        sharPref.edit().putBoolean(SORT_BY_POPULARITY_CHECK_BOX, isChecked).apply()
    }

    fun loadSortByPopularityCheckBoxState() =
        sharPref.getBoolean(SORT_BY_POPULARITY_CHECK_BOX, false)

    fun clearSortByPopularity() {
        sharPref.edit().remove(SORT_BY_POPULARITY).apply()
    }
}