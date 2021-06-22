package com.example.data.network

import com.example.data.apimodels.movies.PopularMoviesApi
import com.example.data.utils.API_KEY
import com.example.data.utils.MOVIES_API_BASE_URL
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import com.example.data.apimodels.movie_details.MovieDetailsModelApi as MovieDetailsModelApi1

interface MoviesApi {

    @GET("/3/movie/popular")
    fun getPopularMovies(): Single<PopularMoviesApi>

    @GET("/3/movie/{movie_id}")
    fun getMoviesDetails(@Path("movie_id") movieId: Int): Single<MovieDetailsModelApi1>

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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(MoviesApi::class.java)
        }
    }
}