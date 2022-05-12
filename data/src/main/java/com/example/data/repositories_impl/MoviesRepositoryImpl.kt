package com.example.data.repositories_impl

import android.graphics.Bitmap
import com.example.data.apimodels.movie_details.MovieDetailsModelApi
import com.example.data.apimodels.movies.MoviesListApiModel
import com.example.data.apimodels.movies.Result
import com.example.data.cache.SettingsDataCache
import com.example.data.db.dao.MoviesDao
import com.example.data.mapers.MoviesApiMapper
import com.example.data.mapers.MoviesEntityMapper
import com.example.data.network.MoviesApi
import com.example.data.utils.SECOND_PAGE
import com.example.domain.models.*
import com.example.domain.repositories.MovieCategoriesRepository
import com.example.domain.repositories.MoviesRepository
import com.example.domain.utils.ResponseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.IOException

class MoviesRepositoryImpl(
    private val moviesApi: MoviesApi,
    private val moviesApiMapper: MoviesApiMapper,
    private val moviesDao: MoviesDao,
    private val movieEntityMapper: MoviesEntityMapper,
    private val settingsDataCache: SettingsDataCache,
    private val movieCategoriesRepository: MovieCategoriesRepository
) : MoviesRepository {

    // This function fetches list of popular movies from server and maps regular
    // popular movie models to popular movie models with details
    override suspend fun getPopularMoviesWithDetails(
        page: Int,
        rating: Int?,
        releaseYear: String?
    ): List<MovieWithDetailsModel> =
        getMovieWithDetailsList(moviesApi.getPopularMovies(page, rating, releaseYear))

    // This function fetches list of upcoming movies from server and maps regular upcoming
    // movie models to popular movie models with details
    override suspend fun getUpcomingMoviesWithDetails(
        page: Int,
        rating: Int?,
        releaseYear: String?
    ): List<MovieWithDetailsModel> =
        getMovieWithDetailsList(moviesApi.getUpcomingMovies(page, rating, releaseYear))

    // This function fetches list of Top Rated movies from server and maps regular Top Rated
    // movie models to popular movie models with details
    override suspend fun getTopRatedMoviesWithDetails(
        page: Int,
        rating: Int?,
        releaseYear: String?
    ): List<MovieWithDetailsModel> =
        getMovieWithDetailsList(moviesApi.getTopRatedMovies(page, rating, releaseYear))

    // This function fetches list of movies selected by genre from server and maps regular movie models to movie models with details
    override suspend fun getMoviesByGenre(
        genreId: String?,
        rating: Int?,
        releaseYear: String?,
        sortByPopularity: String?,
        page: Int?
    ): List<MovieWithDetailsModel> =
        getMovieWithDetailsList(
            moviesApi.getMoviesByGenre(
                genreId,
                rating,
                releaseYear,
                sortByPopularity,
                page
            )
        )

    // This function makes request for list of Similar movies for movie details screen
    override suspend fun getSimilarMovies(movieId: Int): ResponseResult<List<MovieModel>> {

        return try {
            val response = moviesApi.getSimilarMovies(movieId, settingsDataCache.getLanguage())
            if (response.isSuccessful) {
                response.body().let {
                    return@let ResponseResult.Success(moviesApiMapper.mapMovieApiToMovieModelList(it))
                }
            } else {
                ResponseResult.Failure(message = "An unknown error occured")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseResult.Failure(message = "Couldn't reach the server. Check your internet connection")
        }
    }

    // This function makes request for list of Recommendations movies for movie details screen
    override suspend fun getRecommendationsMovies(movieId: Int): ResponseResult<List<MovieModel>> {
        return try {
            val response =
                moviesApi.getRecommendationsMovies(movieId, settingsDataCache.getLanguage())
            if (response.isSuccessful) {
                response.body().let {
                    return@let ResponseResult.Success(moviesApiMapper.mapMovieApiToMovieModelList(it))
                }
            } else {
                ResponseResult.Failure(message = "An unknown error occured")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseResult.Failure(message = "Couldn't reach the server. Check your internet connection")
        }
    }

    // This functions save movie or TV or any media to Watch list on Remote server
    override suspend fun saveToWatchList(
        saveToWatchListModel: SaveToWatchListModel,
        sessionId: String
    ) {
        try {
            val response = moviesApi.saveToWatchList(saveToWatchListModel, sessionId)
            if (response.isSuccessful) {
                response.body()?.let {
                    println("mLog: success = ${it.success}\n${it.status_message}")
                }
            } else println("mLog: success = ${response.body()?.success}\n${response.body()?.status_message}")
        } catch (e: RuntimeException) {
            e.printStackTrace()
        }
    }

    override suspend fun getWatchList(sessionId: String): ResponseResult<List<MovieModel>> {
        val response =
            moviesApi.getWatchList(sessionId, language = settingsDataCache.getLanguage())
        return if (response.isSuccessful) {
            response.body()?.let {
                return@let ResponseResult.Success(moviesApiMapper.mapMovieApiToMovieModelList(it))
            } ?: ResponseResult.Failure(message = "Response body of getting watch list is null")
        } else ResponseResult.Failure(message = "Response of getting watch list is not success")
    }

    override suspend fun getMovieAccountState(
        sessionId: String,
        movieId: Int
    ): ResponseResult<MovieAccountStateModel> {
        return try {
            val response = moviesApi.getMovieAccountState(movieId, sessionId)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let ResponseResult.Success(
                        moviesApiMapper.mapMovieAccountStateApiToModel(
                            it
                        )
                    )
                } ?: ResponseResult.Failure(message = "Get movie account state response is null")
            } else ResponseResult.Failure(message = "Get movie account state response is not successful")
        } catch (e: RuntimeException) {
            e.printStackTrace()
            ResponseResult.Failure(message = "${e.message}")
        }
    }

    // This function gets list of movies by user-entered words
    override suspend fun getMoviesByName(movieName: String, page: Int): List<MovieModel> {
        val response = moviesApi.getMoviesByName(
            movieName = movieName,
            language = settingsDataCache.getLanguage(),
            page = page
        )

        return if (response.isSuccessful) {
            response.body()?.let {
                return@let moviesApiMapper.mapMovieApiToMovieModelList(it)
            } ?: throw IllegalArgumentException("The response is empty")
        } else throw IllegalArgumentException("The response is not successful")
    }

    //This function gets list of video keys for youtube videos
    override suspend fun getTrailersList(movieId: Int): ResponseResult<List<TrailerModel>> {
        return try {
            val response = moviesApi.getTrailerList(movieId, settingsDataCache.getLanguage())
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let ResponseResult.Success(moviesApiMapper.mapTrailerApiToModelsList(it))
                } ?: ResponseResult.Failure(message = "The response is empty")
            } else ResponseResult.Failure(message = "The response is ot successful")
        } catch (e: RuntimeException) {
            e.printStackTrace()
            ResponseResult.Failure(message = "Some error has occurred from network request")
        }
    }

    override suspend fun getTrendingMovie(): ResponseResult<MovieModel> {
        return try {
            val response = moviesApi.getTrendingMovie( language = settingsDataCache.getLanguage())
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let ResponseResult.Success(moviesApiMapper.mapTrendingMoviesListToMovieModel(it))
                }?: ResponseResult.Failure(message = "The response is empty")
            }else ResponseResult.Failure(message = "The response is ot successful")
        } catch (e: IOException) {
            e.printStackTrace()
            ResponseResult.Failure(message = "Some error has occurred from network request")
        }
    }

    // This function makes 19 requests to the server to get list of movies for each of the nineteen genres.
    // All 19 movie lists are displayed on the home screen
    override suspend fun getMoviesSortedByGenre(): ResponseResult<List<MoviesSortedByGenreContainerModel>> {
        val list = mutableListOf<MoviesSortedByGenreContainerModel>()

        return withContext(Dispatchers.IO) {
            val genresList = movieCategoriesRepository.getGenres()
            val runningTasks = genresList.map { genre ->
                async {
                    val response =
                        moviesApi.getMoviesByGenre(
                            genre.id,
                            page = SECOND_PAGE,
                            language = settingsDataCache.getLanguage()
                        )
                    MoviesSortedByGenreContainerModel(
                        genre.id,
                        genre.name,
                        moviesApiMapper.mapMovieApiToMovieModelList(response.body())
                    )
                }
            }
            list.addAll(runningTasks.awaitAll())
            ResponseResult.Success(list)
        }
    }

    // This function takes list of movie api models without details and maps
// it to list of movies with details and returns it
    private suspend fun getMovieWithDetailsList(response: Response<MoviesListApiModel>): List<MovieWithDetailsModel> {

        return if (response.isSuccessful) {
            withContext(Dispatchers.IO) {
                response.body()?.let { body ->
                    val genresList = movieCategoriesRepository.getGenres()
                    val moviesWithDetailsList = body.results.map { apiMovie ->
                        async {
                            getMovieDetailsModelForList(apiMovie.id, genresList, apiMovie)
                        }
                    }
                    moviesWithDetailsList.awaitAll()
                } ?: throw  IllegalArgumentException("An unknown error occured")
            }

        } else throw  IllegalArgumentException("${response.errorBody()?.string()}")

    }


    // This function inserts Movie with details  model to local database
    override suspend fun saveMovieToEntity(movie: MovieWithDetailsModel) {
        try {
            moviesDao.insertSavedMovie(movieEntityMapper.mapMovieModelToEntity(movie))
        } catch (e: Exception) {
            println("mLog: ${e.printStackTrace()}")
        }
    }

    // This function gets all saved movies from local database and maps it to Movie with details models list and return it
    override suspend fun getMovieListFromDb(): Flow<ResponseResult<List<MovieWithDetailsModel>>> {
        return flow {
            moviesDao.getAllSavedMovies().collect { response ->
                try {
                    if (response.isNotEmpty()) {
                        emit(
                            ResponseResult.Success(
                                movieEntityMapper.mapMovieEntityListToModelList(response)
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

    // This function deletes movie from local database by movie id
    override suspend fun deleteMovieById(movieId: List<Int>) {
        try {
            moviesDao.deleteSavedMovieById(movieId)
        } catch (e: Exception) {
            println("mLog: ${e.printStackTrace()}")
        }
    }

    // This function fetches movie details api  model by movie id
    private suspend fun getMovieDetailsForList(movieId: Int): MovieDetailsModelApi? {

        val response = moviesApi.getMoviesDetails(movieId, settingsDataCache.getLanguage())
        return if (response.isSuccessful) {
            response.body()?.let { body ->
                return@let body
            }
        } else {
            println("mLog: The request for $movieId movie id was unsuccessful")
            null
        }
    }

    // This function takes  data such as
// "genres list",  "video api model" , "movie with details model" and maps it all into a MovieWithDetailsModel
// adn returns it for movies with details list
    private suspend fun getMovieDetailsModelForList(
        movieId: Int,
        genreList: List<GenreModel>,
        movieApiModel: Result
    ): MovieWithDetailsModel {

        return try {
            val movieDetails = getMovieDetailsForList(movieId)
            moviesApiMapper.mapMovieDetailsApiToModel(
                movieDetails,
                genreList,
                movieApiModel
            )
        } catch (e: IOException) {
            e.printStackTrace()
            throw IllegalArgumentException("Error!! Please check internet connection")
        }
    }

    // This function takes  data such as
// "video api model" , "movie with details model" and maps it all into a MovieWithDetailsModel
// adn returns it for "Movie details page"
    override suspend fun getMovieDetailsForDetailsPage(movieId: Int): ResponseResult<MovieWithDetailsModel> {

        return try {
            val movieDetails = getMovieDetailsForList(movieId)
            val movieWithDetails = moviesApiMapper.mapMovieDetailsApiToModel(
                movieDetails
            )
            movieWithDetails?.let {
                return@let ResponseResult.Success(it)
            } ?: ResponseResult.Failure(message = "The Movie details object is null")
        } catch (e: IOException) {
            e.printStackTrace()
            ResponseResult.Failure(message = "Error!! Please check internet connection")
        }
    }

    //This function gets movie poster for notification
    override suspend fun getMoviePoster(url: String): ResponseResult<ResponseBody> {
        return try {
            val response = moviesApi.getMoviePoster(url)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let ResponseResult.Success(it)
                } ?: ResponseResult.Failure(message = "The response body is null")
            } else ResponseResult.Failure(message = "The response is not success")
        } catch (e: IOException) {
            e.printStackTrace()
            ResponseResult.Failure(message = "The unknown error. Check the internet connection")
        }
    }


}