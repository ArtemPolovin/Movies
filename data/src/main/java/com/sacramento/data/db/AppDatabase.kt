package com.sacramento.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sacramento.data.db.dao.MoviesDao
import com.sacramento.data.db.tables.SavedMovieEntity
import com.sacramento.data.db.tables.movie_tables.GenreEntity
import com.sacramento.data.db.tables.movie_tables.MovieGenreCrossRef
import com.sacramento.data.db.tables.movie_tables.MovieEntity

@Database(entities = [MovieEntity::class,GenreEntity::class,MovieGenreCrossRef::class, SavedMovieEntity::class], version = 12, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getMoviesDao(): MoviesDao
}