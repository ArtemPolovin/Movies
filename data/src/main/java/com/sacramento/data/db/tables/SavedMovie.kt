package com.sacramento.data.db.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sacramento.domain.models.SavedMovie

@Entity(tableName = "saved_movie")
data class SavedMovieEntity(
    @PrimaryKey(autoGenerate = false)
    val movieId: Int,
    val movieName: String?,
    val rating: String?,
    val poster: String?,
    val vote_count: Int?
):SavedMovie