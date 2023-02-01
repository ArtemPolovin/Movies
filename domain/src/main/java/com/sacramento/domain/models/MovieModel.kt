package com.sacramento.domain.models

data class MovieModel(
    val movieId: Int,
    val poster: String?,
    val title: String?,
    val rating: String?,
    val voteCount: Int?
)