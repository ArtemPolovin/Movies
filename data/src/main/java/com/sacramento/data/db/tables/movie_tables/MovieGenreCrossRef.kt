package com.sacramento.data.db.tables.movie_tables

import androidx.room.Entity

@Entity(primaryKeys = ["movieId","genreId"])
data class MovieGenreCrossRef(
    val movieId: Int,
    val genreId: String
)