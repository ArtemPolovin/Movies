package com.example.domain.repositories

import com.example.domain.models.MovieWithDetailsModel
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
        sortByPopularity: String? = null
    ): List<MovieWithDetailsModel>
}