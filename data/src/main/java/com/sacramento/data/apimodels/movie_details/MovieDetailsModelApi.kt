package com.sacramento.data.apimodels.movie_details

import com.sacramento.domain.models.ApiModel

data class MovieDetailsModelApi(
    val backdrop_path: String?,
    val genres: List<Genre>?,
    val homepage: String?,
    val id: Int,
    val overview: String?,
    val popularity: Double?,
    val poster_path: String?,
    val release_date: String?,
    val title: String?,
    val vote_average: Double?,
    val vote_count: Int?
): ApiModel