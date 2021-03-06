package com.sacramento.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sacramento.data.db.tables.movie_tables.SavedMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedMovie(savedMovie: SavedMovieEntity)

    @Query("DELETE  FROM saved_movie WHERE movieId IN (:movieId)")
    suspend fun deleteSavedMovieById(movieId: List<Int>)

    @Query("SELECT * FROM saved_movie WHERE movieId = :movieId")
    suspend fun getMovieById(movieId: Int): SavedMovieEntity

    @Query("SELECT * FROM saved_movie ")
    fun getAllSavedMovies(): Flow<List<SavedMovieEntity>>
}