package com.example.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.db.dao.MoviesDao
import com.example.data.db.tables.movie_tables.SavedMovieEntity

@Database(entities = [SavedMovieEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getMoviesDao(): MoviesDao
}