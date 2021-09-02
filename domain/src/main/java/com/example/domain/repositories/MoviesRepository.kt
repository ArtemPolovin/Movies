package com.example.domain.repositories

import com.example.domain.models.MovieWithDetailsModel
import com.example.domain.utils.ResponseResult
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    suspend fun getPopularMoviesWithDetails(page: Int): List<MovieWithDetailsModel>
    suspend fun getUpcomingMoviesWithDetails(page: Int): List<MovieWithDetailsModel>
    suspend fun getTopRatedMoviesWithDetails(page: Int): List<MovieWithDetailsModel>
    suspend fun saveMovieToEntity(movie: MovieWithDetailsModel)
    suspend fun getMovieListFromDb(): Flow<ResponseResult<List<MovieWithDetailsModel>>>
    suspend fun deleteMovieById(movieId: List<Int>)
}