package com.example.domain.models

import kotlinx.serialization.Serializable

@Serializable
class PopularMovieModel(
    val releaseData: String,
    val popularityScore: String,
    val movieName: String,
    val rating: Double,
    val poster: String,
    val overview: String,
    val backdropPoster: String,
    val id : Int
)