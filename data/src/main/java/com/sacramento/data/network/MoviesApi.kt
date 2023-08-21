package com.sacramento.data.network

import com.sacramento.data.apimodels.genres.GenresApiModel
import com.sacramento.data.apimodels.movie_details.MovieDetailsModelApi
import com.sacramento.data.apimodels.movie_state.MovieAccountStateApiModel
import com.sacramento.data.apimodels.movies.MoviesListApiModel
import com.sacramento.data.apimodels.trailers.TrailersApiModel
import com.sacramento.data.utils.MOVIES_API_BASE_URL
import com.sacramento.domain.models.SaveToWatchListModel
import com.sacramento.domain.models.SaveToWatchListResponseModel
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.sacramento.data.BuildConfig
import com.sacramento.data.apimodels.reviews.ReviewsApiModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface MoviesApi {

    @GET("/3/movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int,
        @Query("vote_average.gte") rating: Int? = null,
        @Query("primary_release_year") year: String? = null,
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
    ): Response<MoviesListApiModel>

    @GET("/3/discover/movie")
    suspend fun getMoviesByGenre(
        @Query("with_genres") genreId: String?,
        @Query("vote_average.gte") rating: Int? = null,
        @Query("primary_release_year") year: String? = null,
        @Query("sort_by") vote: String? = null,
        @Query("page") page: Int? = null,
        @Query("language") language: String? = null
    ): Response<MoviesListApiModel>

    @GET("/3/movie/{movie_id}")
    suspend fun getMoviesDetails(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String?
    ): Response<MovieDetailsModelApi>

    @GET("/3/genre/movie/list")
    suspend fun getGenresList(
        @Query("language") language: String?
    ): Response<GenresApiModel>

    @GET("/3/movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String?
    ): Response<MoviesListApiModel>

    @GET("/3/movie/{movie_id}/recommendations")
    suspend fun getRecommendationsMovies(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String?
    ): Response<MoviesListApiModel>

    @Headers("Content-Type: application/json;charset=utf-8")
    @POST("/3/account/{account_id}/watchlist")
    suspend fun saveToWatchList(
        @Body saveToWatchListModel: SaveToWatchListModel,
        @Query("session_id") sessionId: String
    ): Response<SaveToWatchListResponseModel>

    @GET("/3/account/{account_id}/watchlist/movies")
    suspend fun getWatchList(
        @Query("session_id") sessionId: String,
        @Query("language") language: String?,
        @Query("page") page: Int
    ): Response<MoviesListApiModel>

    @GET("/3/movie/{movie_id}/account_states")
    suspend fun getMovieAccountState(
        @Path("movie_id") movieId: Int,
        @Query("session_id") sessionId: String
    ): Response<MovieAccountStateApiModel>

    @GET("/3/search/movie")
    suspend fun getMoviesByName(
        @Query("page") page: Int,
        @Query("query") movieName: String,
        @Query("language") language: String?
    ): Response<MoviesListApiModel>

    @GET("/3/movie/{movie_id}/videos")
    suspend fun getTrailerList(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String?
    ): Response<TrailersApiModel>

    @GET("/3/trending/movie/day")
    suspend fun getTrendingMovie(@Query("language") language: String?): Response<MoviesListApiModel>

    @GET
    suspend fun getMoviePoster(@Url url: String): Response<ResponseBody>

    @GET("/3/movie/{movie_id}/reviews")
    suspend fun getMovieReviews(
        @Path("movie_id") movieId: Int,
        @Query("page") page: Int
    ):Response<ReviewsApiModel>


    companion object {
        operator fun invoke(): MoviesApi {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("api_key", BuildConfig.TMDB_APY_KEY)
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
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
                .create(MoviesApi::class.java)
        }
    }
}