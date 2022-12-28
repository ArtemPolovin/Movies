package com.sacramento.data.db.tables.movie_tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genre", primaryKeys = ["genreId","language"])
data class GenreEntity(
    val genreId: String,
    val language: String,
    val genreName: String
)