package com.example.data.apimodels.movie_details

data class MovieDetailsModelApi(
    val backdrop_path: String?,
    val genres: List<Genre>?,
    val homepage: String?,
    val id: Int,
    val overview: String?,
    val popularity: Double?,
    val poster_path: String?,
    val release_date: String?,
    val original_title: String?,
    val vote_average: Double?
)