package com.sacramento.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sacramento.data.db.dao.GuestSessionDao
import com.sacramento.data.db.dao.MoviesDao
import com.sacramento.data.db.tables.SavedMovieEntity
import com.sacramento.data.db.tables.guest_session.GuestSavedMoviesEntity
import com.sacramento.data.db.tables.movie_tables.GenreEntity
import com.sacramento.data.db.tables.movie_tables.MovieEntity
import com.sacramento.data.db.tables.movie_tables.MovieGenreCrossRef

@Database(
    entities = [MovieEntity::class, GenreEntity::class, MovieGenreCrossRef::class, SavedMovieEntity::class, GuestSavedMoviesEntity::class],
    version = 14,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getMoviesDao(): MoviesDao
    abstract fun getGuestSessionDao(): GuestSessionDao
}