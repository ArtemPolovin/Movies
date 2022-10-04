package com.sacramento.data.db.dao

import androidx.room.*
import com.sacramento.data.db.models.MovieWithGenresDBModel
import com.sacramento.data.db.tables.movie_tables.GenreEntity
import com.sacramento.data.db.tables.movie_tables.MovieGenreCrossRef
import com.sacramento.data.db.tables.movie_tables.SavedMovieEntity

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(savedMovie: SavedMovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenre(genres: List<GenreEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieGenreCrossRef(crossRef: List<MovieGenreCrossRef>?)

    @Query("DELETE FROM genre")
    suspend fun deleteFromGenre()

    @Query("DELETE FROM saved_movie")
    suspend fun deleteFromMoives()

    @Query("DELETE FROM moviegenrecrossref")
    suspend fun deleteFromCrossRef()

    @Transaction
    @Query(
        "SELECT * FROM saved_movie JOIN moviegenrecrossref ON " +
                "moviegenrecrossref.movieId = saved_movie.movieId WHERE " +
                "moviegenrecrossref.genreId = :genreId "
    )
    suspend fun getMoviesByGenre(genreId: String?): List<MovieWithGenresDBModel>

    @Transaction
    @Query("SELECT * FROM saved_movie WHERE saved_movie.language = :language ORDER BY popularityScore DESC ")
    suspend fun getMovieSortedByPopularity(language: String): List<MovieWithGenresDBModel>

    @Transaction
    @Query("SELECT * FROM saved_movie WHERE saved_movie.language = :language ORDER BY rating DESC")
    suspend fun getTopRatedMovies(language: String): List<MovieWithGenresDBModel>

    @Transaction
    @Query(
        "SELECT * FROM saved_movie WHERE " +
                "saved_movie.language = :language " +
                "AND releaseData LIKE '%' || :year || '%' " +
                "AND releaseData LIKE '%' || :month || '%' " +
                "ORDER BY popularityScore DESC"
    )
    suspend fun getUpcomingMovies(year: String, month: String, language: String): List<MovieWithGenresDBModel>

    @Transaction
    @Query("SELECT * FROM saved_movie JOIN moviegenrecrossref ON " +
            "moviegenrecrossref.movieId = saved_movie.movieId " +
            "WHERE moviegenrecrossref.genreId = :genreId " +
            "AND saved_movie.language = :language " +
            "AND CASE WHEN :rating NOT NULL THEN saved_movie.rating >= :rating END " +
            "AND CASE WHEN :year NOT NULL THEN releaseData LIKE '%' || :year || '%' END " +
            "ORDER BY CASE WHEN :sortedByPopularity NOT NULL THEN popularityScore END DESC " +
            "LIMIT :limit")
    suspend fun getFilteredMovies(genreId: String? = null,
                                  language: String,
                                  year: String? = null,
                                  rating: Float? = null,
                                  sortedByPopularity: String? = null,
                                  limit: Int
    ): List<MovieWithGenresDBModel>

    @Transaction
    @Query("SELECT * FROM saved_movie  WHERE movieName LIKE '%' || :movieName || '%' LIMIT :limit OFFSET :offset ")
    suspend fun getMoviesByName(movieName: String, limit: Int, offset: Int):  List<MovieWithGenresDBModel>

}