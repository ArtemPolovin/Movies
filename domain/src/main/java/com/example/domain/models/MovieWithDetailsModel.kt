package com.example.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class MovieWithDetailsModel(
    val releaseData: String?,
    val popularityScore: String?,
    val movieName: String?,
    val rating: Float?,
    val poster: String?,
    val overview: String?,
    val backdropImage: String?,
    val genres: String?,
    val homePageUrl: String?,
    val video: String?,
    val id: Int,
    val voteCount: String?
)