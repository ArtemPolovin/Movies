package com.example.domain.models

import kotlinx.serialization.Serializable

@Serializable
class MovieWithDetailsModel(
    val releaseData: String?,
    val popularityScore: String?,
    val movieName: String?,
    val rating: Double?,
    val poster: String?,
    val overview: String?,
    val backdropPoster: String?,
    val genres: String?,
    val homePageUrl: String?,
    val video: String?,
    val id: Int,
    val voteCount: String?
)