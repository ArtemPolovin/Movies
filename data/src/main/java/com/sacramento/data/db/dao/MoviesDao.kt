package com.sacramento.data.db.dao

import androidx.room.*
import com.sacramento.data.db.models.MovieWithGenresDBModel
import com.sacramento.data.db.tables.SavedMovieEntity
import com.sacramento.data.db.tables.movie_tables.GenreEntity
import com.sacramento.data.db.tables.movie_tables.MovieEntity
import com.sacramento.data.db.tables.movie_tables.MovieGenreCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertMovie(savedMovie: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertGenre(genres: List<GenreEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertMovieGenreCrossRef(crossRef: List<MovieGenreCrossRef>?)

    @Query("DELETE FROM genre")
     suspend fun deleteFromGenre()

    @Query("DELETE FROM movie")
     suspend fun deleteFromMoives()

    @Query("DELETE FROM moviegenrecrossref")
     suspend fun deleteFromCrossRef()

    @Query(
        "SELECT  movie.*, genre.* FROM movie " +
                "JOIN moviegenrecrossref ON moviegenrecrossref.movieId = movie.movieId " +
                "JOIN genre ON genre.genreId = moviegenrecrossref.genreId " +
                "WHERE movie.language = :language " +
                "AND genre.language = :language " +
                "ORDER BY popularityScore DESC"
    )
     suspend fun getMovieSortedByPopularity(language: String): Map<MovieEntity, List<GenreEntity>>

    @Query(
        "SELECT  movie.*, genre.* FROM movie " +
                "JOIN moviegenrecrossref ON moviegenrecrossref.movieId = movie.movieId " +
                "JOIN genre ON genre.genreId = moviegenrecrossref.genreId " +
                "WHERE movie.language = :language " +
                "AND genre.language = :language " +
                "ORDER BY rating DESC"
    )
     suspend fun getTopRatedMovies(language: String): Map<MovieEntity, List<GenreEntity>>

    @Query(
        "SELECT  movie.*, genre.* FROM movie " +
                "JOIN moviegenrecrossref ON moviegenrecrossref.movieId = movie.movieId " +
                "JOIN genre ON genre.genreId = moviegenrecrossref.genreId " +
                "WHERE movie.language = :language " +
                "AND genre.language = :language " +
                "AND releaseData LIKE '%' || :year || '%' " +
                "AND releaseData LIKE '%' || :month || '%' " +
                "ORDER BY popularityScore DESC"
    )
     suspend fun getUpcomingMovies(
        year: String,
        month: String,
        language: String
    ): Map<MovieEntity, List<GenreEntity>>


    @Query(
        "SELECT  movie.*, genre.* FROM movie " +
                "JOIN moviegenrecrossref ON moviegenrecrossref.movieId = movie.movieId " +
                "JOIN genre ON genre.genreId = moviegenrecrossref.genreId " +
                "WHERE movie.language = :language " +
                "AND genre.language = :language " +
                "AND movie.movieId IN " +
                "(" +
                "SELECT movie.movieId FROM movie " +
                "JOIN moviegenrecrossref ON moviegenrecrossref.movieId = movie.movieId " +
                "JOIN genre ON genre.genreId = moviegenrecrossref.genreId " +
                "WHERE moviegenrecrossref.genreId = :genreId " +
                "AND movie.language = :language " +
                "AND genre.language = :language " +
                "AND CASE WHEN :rating NOT NULL THEN rating >= :rating ELSE 1 == 1 END " +
                "AND CASE WHEN :year NOT NULL THEN releaseData LIKE '%' || :year || '%' ELSE 1 == 1 END" +
                ")" +
                "ORDER BY CASE WHEN :sortedByPopularity NOT NULL THEN movie.popularityScore END DESC " +
                "LIMIT :limit")
     suspend fun getFilteredMovies(
        genreId: String? = null,
        language: String,
        limit: Int,
        sortedByPopularity: String? = null,
        rating: Float? = null,
        year: String? = null
    ): Map<MovieEntity, List<GenreEntity>>



    @Transaction
    @Query("SELECT * FROM movie  WHERE movieName LIKE '%' || :movieName || '%' LIMIT :limit OFFSET :offset ")
     suspend fun getMoviesByName(
        movieName: String,
        limit: Int,
        offset: Int
    ): List<MovieWithGenresDBModel>

    @Query(
        "SELECT  movie.*, genre.* FROM movie " +
                "JOIN moviegenrecrossref ON moviegenrecrossref.movieId = movie.movieId " +
                "JOIN genre ON genre.genreId = moviegenrecrossref.genreId " +
                "WHERE movie.language = :language " +
                "AND genre.language = :language " +
                "AND movie.movieId IN " +
                "(" +
                "SELECT movie.movieId FROM movie " +
                "JOIN moviegenrecrossref ON moviegenrecrossref.movieId = movie.movieId " +
                "JOIN genre ON genre.genreId = moviegenrecrossref.genreId " +
                "WHERE moviegenrecrossref.genreId = :genreId " +
                "AND movie.language = :language " +
                "AND genre.language = :language " +
                ")" +
                "LIMIT :limit " +
                "OFFSET :offset"
    )
     suspend fun getMoviesByGenre(
        genreId: String,
        language: String,
        limit: Int,
        offset: Int = 0
    ): Map<MovieEntity, List<GenreEntity>>


    @Query(
        "SELECT  movie.*, genre.* FROM movie " +
                "JOIN moviegenrecrossref ON moviegenrecrossref.movieId = movie.movieId " +
                "JOIN genre  ON genre.genreId = moviegenrecrossref.genreId " +
                "WHERE movie.movieId = :movieId " +
                "AND movie.language = :language " +
                "AND genre.language = :language"
    )
     suspend fun getMovieById(
        movieId: Int,
        language: String
    ): Map<MovieEntity, List<GenreEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertAllSavedMovies(savedMovies: List<SavedMovieEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
     suspend fun insertSavedMovie(savedMovie: SavedMovieEntity)

    @Query("DELETE  FROM saved_movie WHERE saved_movie.movieId = :movieId")
     suspend fun removeSavedMovieById(movieId: String)

    @Query("SELECT * FROM saved_movie LIMIT :limit OFFSET :offset")
     suspend fun getSavedMovies(limit: Int, offset: Int): List<SavedMovieEntity>

    @Query("DELETE FROM saved_movie")
     suspend fun deleteAllMovies()


}