package com.sacramento.domain.repositories

import com.sacramento.domain.models.*
import com.sacramento.domain.utils.ResponseResult
import kotlinx.coroutines.flow.Flow

interface  MovieDBRepository {
    suspend fun insertMoviesToDB(movieApi: ApiModel)

    suspend fun getMoviesFromDBSortedByPopularity(): List<MovieWithDetailsModel>

    suspend fun getTopRatedMovies(): List<MovieWithDetailsModel>

    suspend fun getUpcomingMovies(): List<MovieWithDetailsModel>

    suspend fun getMoviesByNameFromDB(movieName: String, limit: Int, offset: Int):  List<MovieModel>

    suspend fun getFilteredMovies(
        genreId: String?,
        year: String?,
        rating: Float?,
        sortedByPopularity: String?,
        limit: Int
    ): List<MovieWithDetailsModel>

    suspend fun getMovieById(movieId: Int): ResponseResult<MovieWithDetailsModel>

    suspend fun getSimilarMovies(movieId: Int):ResponseResult<List<MovieModel>>
    suspend fun getRecommendationsMovies(movieId: Int): ResponseResult<List<MovieModel>>

    suspend fun insertAllSavedMoviesFromAccount(savedMovies: List<MovieModel>)
    suspend fun insertSavedMovie(savedMovie: MovieModel)
    suspend fun removeSavedMovieById(movieId: String)
    suspend fun getSavedMovies(limit: Int, offset: Int): List<MovieModel>
    suspend fun getMoviesSortedByGenreFromDB(): ResponseResult<List<MoviesSortedByGenreContainerModel>>
    suspend fun clearSavedMoviesDB()
}