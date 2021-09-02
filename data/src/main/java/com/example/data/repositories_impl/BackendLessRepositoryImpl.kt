package com.example.data.repositories_impl

import com.example.data.mapers.BackendLessMapper
import com.example.data.network.BackendLessService
import com.example.domain.models.MovieCategoriesCellModel
import com.example.domain.repositories.BackendLessRepository
import com.example.domain.utils.ResponseResult
import java.lang.Exception

class BackendLessRepositoryImpl(
   private val backendLessService: BackendLessService,
   private val backendLessMapper: BackendLessMapper
): BackendLessRepository {

    override suspend fun getMovieCategoriesImages(): ResponseResult<List<MovieCategoriesCellModel>> {
        return try {
            val response = backendLessService.getMovieCategoriesImages()
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    return@let ResponseResult.Success(
                        backendLessMapper.mapMovieCategoryApiToModelList(body)
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
}