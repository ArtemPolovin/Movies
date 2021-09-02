package com.example.data.network

import com.example.data.apimodels.movie_categories.MovieCategoriesApiModel
import com.example.data.utils.BACKEND_LESS_URL
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface BackendLessService {

    @GET("/api/data/Movie_categories")
    suspend fun getMovieCategoriesImages(): Response<MovieCategoriesApiModel>

    companion object {
        operator fun invoke(): BackendLessService {
            return Retrofit.Builder()
                .baseUrl(BACKEND_LESS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(BackendLessService::class.java)
        }
    }
}