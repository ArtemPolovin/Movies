package com.sacramento.data.db.tables.guest_session

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "guest_saved_movies")
data class GuestSavedMoviesEntity(
    @PrimaryKey(autoGenerate = false)
    val movie_id: Int,
    val poster: String?,
    val title: String?,
    val rating: Double?,
    val vote_count: Int?
)