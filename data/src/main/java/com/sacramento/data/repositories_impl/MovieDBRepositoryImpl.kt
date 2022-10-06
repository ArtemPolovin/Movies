package com.sacramento.data.repositories_impl

import com.sacramento.data.apimodels.movie_details.MovieDetailsModelApi
import com.sacramento.data.cache.SettingsDataCache
import com.sacramento.data.db.dao.MoviesDao
import com.sacramento.data.mapers.MoviesEntityMapper
import com.sacramento.data.utils.DEFAULT_ENGLISH_LANGUAGE_VALUE
import com.sacramento.data.utils.MONTH
import com.sacramento.data.utils.YEAR
import com.sacramento.domain.models.ApiModel
import com.sacramento.domain.models.MovieModel
import com.sacramento.domain.models.MovieWithDetailsModel
import com.sacramento.domain.repositories.MovieDBRepository
import java.text.SimpleDateFormat
import java.util.*

class MovieDBRepositoryImpl(
    private val moviesDao: MoviesDao,
    private val movieEntityMapper: MoviesEntityMapper,
    private val settingsDataCache: SettingsDataCache
) : MovieDBRepository {
   private val curYear = SimpleDateFormat(YEAR, Locale.getDefault()).format(Date())
   private val curMonth = SimpleDateFormat(MONTH, Locale.getDefault()).format(Date())

    override suspend fun insertMoviesToDB(movieApi: ApiModel) {
        moviesDao.run {
            insertMovie(movieEntityMapper.mapMovieModelToEntity(movieApi as MovieDetailsModelApi))
            insertMovieGenreCrossRef(
                movieEntityMapper.mapMovieApiToMovieGenreCrossRefEntity(
                    movieApi
                )
            )
        }
    }

    override suspend fun getMoviesFromDBSortedByPopularity(): List<MovieWithDetailsModel> {
        val curLanguage = settingsDataCache.getLanguage() ?: DEFAULT_ENGLISH_LANGUAGE_VALUE
        val movies = moviesDao.getMovieSortedByPopularity(curLanguage)
        return movieEntityMapper.mapMovieEntityListToMovieWithDetailsModelList(movies)
    }

    override suspend fun getTopRatedMovies(): List<MovieWithDetailsModel> {
        val curLanguage = settingsDataCache.getLanguage() ?: DEFAULT_ENGLISH_LANGUAGE_VALUE
        val movies = moviesDao.getTopRatedMovies(curLanguage)
        return movieEntityMapper.mapMovieEntityListToMovieWithDetailsModelList(movies)
    }

    override suspend fun getUpcomingMovies(): List<MovieWithDetailsModel> {
        val curLanguage = settingsDataCache.getLanguage() ?: DEFAULT_ENGLISH_LANGUAGE_VALUE

        val movies = moviesDao.getUpcomingMovies(curYear, curMonth,curLanguage)
        return movieEntityMapper.mapMovieEntityListToMovieWithDetailsModelList(movies)
    }

    override suspend fun getMoviesByNameFromDB(
        movieName: String,
        limit: Int,
        offset: Int
    ): List<MovieModel> {
        val movies = moviesDao.getMoviesByName(movieName, limit, offset)
        return movieEntityMapper.mapMovieEntityListToMovieModelList(movies)
    }

    override suspend fun getFilteredMovies(
        genreId: String?,
        year: String?,
        rating: Float?,
        sortedByPopularity: String?,
        limit: Int
    ): List<MovieWithDetailsModel> {
        val curLanguage = settingsDataCache.getLanguage() ?: DEFAULT_ENGLISH_LANGUAGE_VALUE
        val movies = moviesDao.getFilteredMovies(
            genreId = genreId,
            year = year,
            rating = rating,
            sortedByPopularity = sortedByPopularity,
            limit = limit,
            language = curLanguage
        )
        return movieEntityMapper.mapMovieEntityListToMovieWithDetailsModelList(movies)
    }
}