package com.example.data.repositories_impl

import com.example.data.apimodels.movie_details.MovieDetailsModelApi
import com.example.data.apimodels.movies.Result
import com.example.data.apimodels.video.VideoApiModel
import com.example.data.db.dao.MoviesDao
import com.example.data.mapers.MoviesApiMapper
import com.example.data.mapers.MoviesEntityMapper
import com.example.data.network.MoviesApi
import com.example.data.utils.SettingsDataCache
import com.example.domain.models.MovieWithDetailsModel
import com.example.domain.repositories.MoviesRepository
import com.example.domain.utils.ResponseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException


class MoviesRepositoryImpl(
    private val moviesApi: MoviesApi,
    private val moviesApiMapper: MoviesApiMapper,
    private val moviesDao: MoviesDao,
    private val movieEntityMapper: MoviesEntityMapper,
    private val settingsDataCache: SettingsDataCache
) : MoviesRepository {

    override suspend fun getPopularMoviesWithDetails(page: Int): List<MovieWithDetailsModel> {
        val moviesIdList = moviesApi.getPopularMovies(page).results
        val movieDetails = getMovieDetailsList(moviesIdList)
        val videosList = getVideosList(moviesIdList)
        return moviesApiMapper.mapMovieDetailsAndMoviesToModelList(movieDetails, moviesIdList,videosList)
    }

    override suspend fun getUpcomingMoviesWithDetails(page: Int): List<MovieWithDetailsModel> {
      return  try {
            val response = moviesApi.getUpcomingMovies(page)
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    val moviesIdList = body.results
                    val movieDetails = getMovieDetailsList(moviesIdList)
                    val videosList = getVideosList(moviesIdList)
                    return@let moviesApiMapper.mapMovieDetailsAndMoviesToModelList(
                            movieDetails,
                            moviesIdList,
                            videosList
                        )
                } ?: throw  IllegalArgumentException("An unknown error occured")
            } else throw  IllegalArgumentException("${response.errorBody()?.string()}")
        } catch (e: IOException) {
            e.printStackTrace()
          throw  IllegalArgumentException("Please check the internet connection")
        }
    }

    override suspend fun getTopRatedMoviesWithDetails(page: Int): List<MovieWithDetailsModel> {
        return  try {
            val response = moviesApi.getTopRatedMovies(page)
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    val moviesIdList = body.results
                    val movieDetails = getMovieDetailsList(moviesIdList)
                    val videosList = getVideosList(moviesIdList)
                    return@let moviesApiMapper.mapMovieDetailsAndMoviesToModelList(
                        movieDetails,
                        moviesIdList,
                        videosList
                    )
                } ?: throw  IllegalArgumentException("An unknown error occured")
            } else throw  IllegalArgumentException("${response.errorBody()?.string()}")
        } catch (e: IOException) {
            e.printStackTrace()
            throw  IllegalArgumentException("Please check the internet connection")
        }
    }

    override suspend fun saveMovieToEntity(movie: MovieWithDetailsModel) {
        try {
            moviesDao.insertSavedMovie(movieEntityMapper.mapMovieModelToEntity(movie))
        } catch (e: Exception) {
            println("mLog: ${e.printStackTrace()}")
        }
    }

    override suspend fun getMovieListFromDb(): Flow<ResponseResult<List<MovieWithDetailsModel>>> {
        return flow {
            moviesDao.getAllSavedMovies().collect { response ->
                try {
                    if (response.isNotEmpty()) {
                        emit(
                            ResponseResult.Success(movieEntityMapper.mapMovieEntityListToModelList(response)
                            )
                        )
                    } else {
                        emit(ResponseResult.Failure(message = "The list is empty"))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    emit(ResponseResult.Failure(message = "Some error has occurred"))
                }

            }
        }.flowOn(Dispatchers.Default)
    }

    override suspend fun deleteMovieById(movieId: List<Int>) {
        try {
            moviesDao.deleteSavedMovieById(movieId)
        } catch (e: Exception) {
            println("mLog: ${e.printStackTrace()}")
        }
    }

    private suspend fun getMovieDetails(movieId: Int): MovieDetailsModelApi {

        return try {
            val response = moviesApi.getMoviesDetails(movieId,settingsDataCache.getLanguage())
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    return@let body
                } ?: throw IllegalArgumentException("An unknown error occured")
            } else {
                throw IllegalArgumentException("Response was not successful")
            }
        } catch (e: IOException) {
            e.printStackTrace()
            throw IllegalArgumentException("Some problem with API connection")
        }
    }

    private suspend fun getVideo(movieId: Int): VideoApiModel {
        return try {
            val response = moviesApi.getVideo(movieId,settingsDataCache.getLanguage())
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    return@let body
                } ?: throw IllegalArgumentException("An unknown error occured")
            } else {
                throw IllegalArgumentException("Response was not successful")
            }
        } catch (e: IOException) {
            e.printStackTrace()
            throw IllegalArgumentException("Some problem with API connection")
        }
    }

    private suspend fun getMovieDetailsList(movieIdList: List<Result>): List<MovieDetailsModelApi> {
           return movieIdList.map { getMovieDetails(it.id) }

    }

    private suspend fun getVideosList(movieIdList: List<Result>): List<VideoApiModel> {
        return movieIdList.map { getVideo(it.id) }
    }


}