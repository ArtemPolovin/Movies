package com.example.data.repositories_impl

import com.example.data.apimodels.genres.GenresApiModel
import com.example.data.mapers.BackendLessMapper
import com.example.data.network.BackendLessService
import com.example.data.network.MoviesApi
import com.example.domain.models.MovieCategoriesCellModel
import com.example.domain.repositories.BackendLessRepository
import com.example.domain.utils.ResponseResult
import java.lang.Exception

class BackendLessRepositoryImpl(
   private val backendLessService: BackendLessService,
   private val backendLessMapper: BackendLessMapper,
   private val moviesApi: MoviesApi
): BackendLessRepository {

    override suspend fun getMovieCategoriesImages(): ResponseResult<List<MovieCategoriesCellModel>> {

        val genres = getGenres()

         if (genres is ResponseResult.Failure) {
          return  ResponseResult.Failure(message = "The Genres api request is not success")

        }else if(genres is ResponseResult.Success) {

          return  try {
                val responseCategories = backendLessService.getMovieCategoriesImages()
                if (responseCategories.isSuccessful) {
                    responseCategories.body()?.let { body ->
                        return@let ResponseResult.Success(
                            backendLessMapper.mapMovieCategoryApiToModelList(body,genres.data)
                        )
                    } ?: ResponseResult.Failure(message = "An unknown error occured")
                } else {
                    ResponseResult.Failure(message = "The response is not success")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                ResponseResult.Failure(e, message = "Check connection to internet")
            }
        }

      return ResponseResult.Failure(message = "Error")
    }

    private suspend fun getGenres(): ResponseResult<GenresApiModel> {
      return  try{
            val response = moviesApi.getGenresList()
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let ResponseResult.Success(it)
                }?: ResponseResult.Failure(message = "An unknown error occured")
            } else {
                ResponseResult.Failure(message = "The response is not success")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseResult.Failure(e, message = "Check connection to internet")
        }
    }
}