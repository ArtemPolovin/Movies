package com.example.domain.repositories

import com.example.domain.models.*
import com.example.domain.utils.ResponseResult
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    suspend fun getPopularMoviesWithDetails(
        page: Int,
        rating: Int? = null,
        releaseYear: String? = null
    ): List<MovieWithDetailsModel>

    suspend fun getUpcomingMoviesWithDetails(
        page: Int,
        rating: Int? = null,
        releaseYear: String? = null
    ): List<MovieWithDetailsModel>

    suspend fun getTopRatedMoviesWithDetails(
        page: Int,
        rating: Int? = null,
        releaseYear: String? = null
    ): List<MovieWithDetailsModel>

    suspend fun saveMovieToEntity(movie: MovieWithDetailsModel)
    suspend fun getMovieListFromDb(): Flow<ResponseResult<List<MovieWithDetailsModel>>>
    suspend fun deleteMovieById(movieId: List<Int>)
    suspend fun getMoviesByGenre(
        genreId: String?,
        rating: Int? = null,
        releaseYear: String? = null,
        sortByPopularity: String? = null,
        page: Int? = null
    ): List<MovieWithDetailsModel>

    suspend fun getMoviesSortedByGenre(): ResponseResult<List<MoviesSortedByGenreContainerModel>>

    suspend fun getMovieDetailsForDetailsPage(movieId: Int): ResponseResult<MovieWithDetailsModel>

    suspend fun getSimilarMovies(movieId: Int): ResponseResult<List<MovieModel>>
    suspend fun getRecommendationsMovies(movieId: Int): ResponseResult<List<MovieModel>>

    suspend fun saveToWatchList(saveToWatchListModel: SaveToWatchListModel,sessionId: String)
    suspend fun getWatchList(sessionId: String): Flow<ResponseResult<List<MovieModel>>>
    suspend fun getMovieAccountState(sessionId: String,movieId: Int): ResponseResult<MovieAccountStateModel>


}