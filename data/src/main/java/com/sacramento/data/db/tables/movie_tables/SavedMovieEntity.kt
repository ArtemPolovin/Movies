package com.sacramento.data.db.tables.movie_tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_movie", primaryKeys = ["movieId","language"])
data class SavedMovieEntity(
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