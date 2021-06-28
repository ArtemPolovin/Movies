package com.example.domain.repositories

import com.example.domain.models.PopularMovieWithDetailsModel
import com.example.domain.models.utils.ResponseResult

interface MoviesRepository {

    suspend fun getMoviesWithDetails(page: Int) : List<PopularMovieWithDetailsModel>
    suspend fun saveMovieToEntity(movie: PopularMovieWithDetailsModel)
    suspend fun getMovieListFromDb(): ResponseResult<List<PopularMovieWithDetailsModel>>
    suspend fun deleteMovieById(movieId: Int)
}