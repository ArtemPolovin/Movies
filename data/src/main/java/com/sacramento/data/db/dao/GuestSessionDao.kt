package com.sacramento.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sacramento.data.db.tables.guest_session.GuestSavedMoviesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GuestSessionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGuestMovie(guestSavedMovie: GuestSavedMoviesEntity)

    @Query("SELECT * FROM guest_saved_movies LIMIT :limit OFFSET :offset")
    suspend fun getAllGuestSavedMovies(limit: Int, offset: Int): List<GuestSavedMoviesEntity>

    @Query("DELETE FROM guest_saved_movies WHERE movie_id = :movieId")
    suspend fun deleteGuestMovie(movieId: Int)

    @Query("DELETE FROM guest_saved_movies WHERE movie_id IN (:movieIdList)")
    suspend fun deleteListOfGuestWatchList(movieIdList: List<Int>)

    @Query("SELECT * FROM guest_saved_movies WHERE movie_id = :movieId")
    suspend fun getSavedMovieById(movieId: Int): GuestSavedMoviesEntity?

}