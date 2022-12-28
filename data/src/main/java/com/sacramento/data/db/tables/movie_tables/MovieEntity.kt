package com.sacramento.data.db.tables.movie_tables

import androidx.room.Entity

@Entity(tableName = "movie", primaryKeys = ["movieId","language"])
data class MovieEntity(
    val movieId: Int,
    val language: String,
    val releaseData: String?,
    val popularityScore: Double?,
    val movieName: String?,
    val rating: Float?,
    val poster: String?,
    val overview: String?,
    val backdropPoster: String?,
    val homePageUrl: String?,
    val vote_count: String?
)