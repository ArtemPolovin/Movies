package com.sacramento.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sacramento.data.db.dao.MoviesDao
import com.sacramento.data.db.tables.movie_tables.SavedMovieEntity

@Database(entities = [SavedMovieEntity::class], version = 4, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getMoviesDao(): MoviesDao
}