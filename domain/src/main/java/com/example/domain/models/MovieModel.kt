package com.example.domain.models

data class MovieModel(
    val movieId: Int,
    val poster: String?,
    val title: String?,
    val rating: Double?,
    val voteCount: Int?
)