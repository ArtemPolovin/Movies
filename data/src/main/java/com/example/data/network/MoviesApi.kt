package com.example.data.network

import com.example.data.apimodels.genres.GenresApiModel
import com.example.data.apimodels.movie_details.MovieDetailsModelApi
import com.example.data.apimodels.movies.MoviesListApiModel
import com.example.data.apimodels.video.VideoApiModel
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
    suspend fun getPopularMovies(
        @Query("page") page: Int,
        @Query("vote_average.gte") rating: Int? = null,
        @Query("primary_release_year") year: String? = null,
       // @Query("with_genres") gener: String = "53",
        // @Query("certification") sert: String = "R",
        // @Query("vote_count") voteCount : Int? = null,

        /*@Query("primary_release_date.lte") date : String? = "2020-10-01",
        @Query("primary_release_date.gte") date2 : String? = "2018-10-01",
        @Query("sort_by") sortBy: String = "primary_release_date.desc",
        @Query("sort_by") popular: String = "popularity.desc",*/

    ): Response<MoviesListApiModel>

    @GET("/3/movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("page") page: Int,
        @Query("vote_average.gte") rating: Int? = null,
        @Query("primary_release_year") year: String? = null
    ): Response<MoviesListApiModel>

    @GET("/3/movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int,
        @Query("vote_average.gte") rating: Int? = null,
        @Query("primary_release_year") year: String? = null
        /*@Query("primary_release_year") year: Int = 2010,
        @Query("with_genres") gener: Int = 53,
        @Query("sort_by") vote : String = "vote_average.desc",
        @Query("vote_count") voteCount : Int = 10*/
    ): Response<MoviesListApiModel>

    @GET("/3/discover/movie")
    suspend fun getMoviesByGenre(
        @Query("with_genres") genreId: String?,
        @Query("vote_average.gte") rating: Int? = null,
        @Query("primary_release_year") year: String? = null,
        @Query("sort_by") vote : String? = null
    ): Response<MoviesListApiModel>

    @GET("/3/movie/{movie_id}/videos")
    suspend fun getVideo(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String?
    ): Response<VideoApiModel>

    @GET("/3/movie/{movie_id}")
    suspend fun getMoviesDetails(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String?
    ): Response<MovieDetailsModelApi>


    companion object {
        operator fun invoke(): MoviesApi {
            val requestInterceptor = Interceptor { chain ->
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