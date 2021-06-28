package com.example.data.repositories_impl

import com.example.data.apimodels.movie_details.MovieDetailsModelApi
import com.example.data.db.dao.MoviesDao
import com.example.data.mapers.MoviesApiMapper
import com.example.data.mapers.MoviesEntityMapper
import com.example.data.network.MoviesApi
import com.example.domain.models.PopularMovieWithDetailsModel
import com.example.domain.models.utils.ResponseResult
import com.example.domain.repositories.MoviesRepository
import java.lang.Exception


class MoviesRepositoryImpl(
    private val moviesApi: MoviesApi,
    private val moviesApiMapper: MoviesApiMapper,
    private val moviesDao: MoviesDao,
    private val  movieEntityMapper: MoviesEntityMapper
    ):MoviesRepository  {

   override  suspend fun getMoviesWithDetails(page: Int): List<PopularMovieWithDetailsModel> {
        val movieResponse = moviesApi.getPopularMovies(page).results
        val movieDetails: List<MovieDetailsModelApi> = moviesApi.getPopularMovies(page).results.map {
            moviesApi.getMoviesDetails(it.id)
        }
        return moviesApiMapper.mapMovieDetailsAndMoviesToModelList(movieDetails,movieResponse)
    }

    override suspend fun saveMovieToEntity(movie: PopularMovieWithDetailsModel) {
        try {
            moviesDao.insertSavedMovie(movieEntityMapper.mapMovieModelToEntity(movie))
        } catch (e: Exception) {
            println("mLog: ${e.printStackTrace()}")
        }
    }

    override suspend fun getMovieListFromDb(): ResponseResult<List<PopularMovieWithDetailsModel>> {
        return try {
            val response = moviesDao.getAllSavedMovies()
            if (response.isNotEmpty()) {
                ResponseResult.Success(movieEntityMapper.mapMovieEntityListToModelList(response))
            } else {
                ResponseResult.Failure(message = "The list is empty")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseResult.Failure(message = "Some error has occurred")
        }
    }

    override suspend fun deleteMovieById(movieId: Int) {
        try {
            moviesDao.deleteSavedMovieById(movieId)
        } catch (e: Exception) {
            println("mLog: ${e.printStackTrace()}")
        }
    }


}