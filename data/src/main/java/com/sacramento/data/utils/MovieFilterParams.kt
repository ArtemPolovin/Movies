package com.sacramento.data.utils

import com.sacramento.data.cache.SharedPrefMovieCategory
import com.sacramento.data.cache.SharedPrefMovieFilter

data class MovieFilterParams(
    private val sharedPrefMovieCategory: SharedPrefMovieCategory,
    private val shardPrefMovieFilter: SharedPrefMovieFilter
) {

    fun getMovieCategory() = sharedPrefMovieCategory.loadMovieCategory()
    fun getGenreId() = sharedPrefMovieCategory.loadGenreId()
    fun getRating() = shardPrefMovieFilter.loadRating()
    fun getReleaseYear() = shardPrefMovieFilter.loadReleaseYear()
    fun getSortByPopulationState() = shardPrefMovieFilter.loadSortByPopularity()
}