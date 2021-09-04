package com.example.data.network

import com.example.data.apimodels.movie_details.MovieDetailsModelApi
import com.example.data.apimodels.movies.MoviesListApiModel
import com.example.data.utils.API_KEY
import com.example.data.utils.MOVIES_API_BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MoviesApi {

    @GET("/3/movie/popular")
   suspend fun getPopularMovies(@Query("page") page: Int): MoviesListApiModel

   @GET("/3/movie/upcoming")
   suspend fun getUpcomingMovies(@Query("page") page: Int): Response<MoviesListApiModel>

    @GET("/3/movie/top_rated")
    suspend fun getTopRatedMovies(@Query("page") page: Int): Response<MoviesListApiModel>

    @GET("/3/movie/{movie_id}")
   suspend fun getMoviesDetails(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String?
    ): Response<MovieDetailsModelApi>

    companion object{
        operator fun invoke(): MoviesApi{
            val requestInterceptor = Interceptor{chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("api_key", API_KEY)
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()
                return@Interceptor chain.proceed(request)

            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor).build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(MOVIES_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MoviesApi::class.java)
        }
    }
}