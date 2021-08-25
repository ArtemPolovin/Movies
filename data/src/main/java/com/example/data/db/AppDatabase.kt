package com.example.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.db.dao.MoviesDao
import com.example.data.db.tables.movie_tables.SavedMovieEntity

@Database(entities = [SavedMovieEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getMoviesDao(): MoviesDao
}