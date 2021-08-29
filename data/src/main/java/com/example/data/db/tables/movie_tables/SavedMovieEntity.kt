package com.example.data.db.tables.movie_tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_movie")
class SavedMovieEntity(
    @PrimaryKey(autoGenerate = false)
    val movieId: Int,
    val releaseData: String?,
    val popularityScore: String?,
    val movieName: String?,
    val rating: Double?,
    val poster: String?,
    val overview: String?,
    val backdropPoster: String?,
    val genres: String?,
    val homePageUrl: String?,
)