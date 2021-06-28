package com.example.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.db.tables.movie_tables.SavedMovieEntity

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedMovie(savedMovie: SavedMovieEntity)

    @Query("DELETE  FROM saved_movie WHERE movieId = :movieId")
    suspend fun deleteSavedMovieById(movieId: Int)

    @Query("SELECT * FROM saved_movie WHERE movieId = :movieId")
    suspend fun getMovieById(movieId: Int): SavedMovieEntity

    @Query("SELECT * FROM saved_movie ")
    suspend fun getAllSavedMovies(): List<SavedMovieEntity>
}