package com.example.domain.repositories

import com.example.domain.models.PopularMovieWithDetailsModel
import com.example.domain.utils.ResponseResult
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    suspend fun getMoviesWithDetails(page: Int): List<PopularMovieWithDetailsModel>
    suspend fun saveMovieToEntity(movie: PopularMovieWithDetailsModel)
    suspend fun getMovieListFromDb(): Flow<ResponseResult<List<PopularMovieWithDetailsModel>>>
    suspend fun deleteMovieById(movieId: List<Int>)
}