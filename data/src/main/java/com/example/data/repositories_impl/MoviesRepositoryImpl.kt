package com.example.data.repositories_impl

import com.example.data.apimodels.movie_details.MovieDetailsModelApi
import com.example.data.apimodels.movies.MoviesListApiModel
import com.example.data.apimodels.movies.Result
import com.example.data.apimodels.video.VideoApiModel
import com.example.data.cache.SettingsDataCache
import com.example.data.db.dao.MoviesDao
import com.example.data.mapers.MoviesApiMapper
import com.example.data.mapers.MoviesEntityMapper
import com.example.data.network.MoviesApi
import com.example.data.utils.SECOND_PAGE
import com.example.domain.models.GenreModel
import com.example.domain.models.MovieModel
import com.example.domain.models.MovieWithDetailsModel
import com.example.domain.models.MoviesSortedByGenreContainerModel
import com.example.domain.repositories.MovieCategoriesRepository
import com.example.domain.repositories.MoviesRepository
import com.example.domain.utils.ResponseResult
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import retrofit2.Response
import java.io.IOException
import java.util.*

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
            val response = moviesApi.getRecommendationsMovies(movieId, settingsDataCache.getLanguage())
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
            try {
                list.addAll(runningTasks.awaitAll())
                ResponseResult.Success(list)
            } catch (e: IOException) {
                e.printStackTrace()
                ResponseResult.Failure(message = "An unknown error occured")
            }

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
                             getMovieDetailsModelForList(apiMovie.id,genresList,apiMovie)
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

    // This function takes list of regular movies api without details ids and based on the received
    // list of ids the function gets list of movie details api models
   /* private suspend fun getMovieDetailsApiList(movieIdList: List<Int>): List<MovieDetailsModelApi?> {

        val movieDetailsApiList: List<MovieDetailsModelApi?>
        withContext(Dispatchers.IO) {
            val runningTask = movieIdList.map { movieId ->
                async {
                    getMovieDetailsForList(movieId)
                }
            }
            movieDetailsApiList = runningTask.awaitAll()
        }
        return movieDetailsApiList
    }*/

    // This function takes list of regular movies api without details ids and based on the
    // received list of ids the function gets list of video models for each movie id
  /*  private suspend fun getVideosList(movieIdList: List<Int>): List<VideoApiModel?> {

        val videoApiModelList: List<VideoApiModel?>
        withContext(Dispatchers.IO) {
            val runningTask = movieIdList.map { movieId ->
                async { getVideo(movieId) }
            }
            videoApiModelList = runningTask.awaitAll()
        }
        return videoApiModelList
    }*/

    // This function fetches video model by movie id
    private suspend fun getVideo(movieId: Int): VideoApiModel? {

        val response = moviesApi.getVideo(movieId, settingsDataCache.getLanguage())
        return if (response.isSuccessful) {
            response.body()?.let { body ->
                return@let body
            }
        } else {
            println("mLog: The request for $movieId movie id was unsuccessful")
            null
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
    private suspend fun getMovieDetailsModelForList(movieId: Int, genreList: List<GenreModel>, movieApiModel: Result): MovieWithDetailsModel {
        return withContext(Dispatchers.IO) {
            val movieDetails = async { getMovieDetailsForList(movieId) }
            val video = async { getVideo(movieId) }

              try {
                  return@withContext moviesApiMapper.mapMovieDetailsApiToModel(
                    movieDetails.await(),
                    video.await(),
                    genreList,
                    movieApiModel
                )
            } catch (e: IOException) {
                e.printStackTrace()
                throw java.lang.IllegalArgumentException("Error!! Please check internet connection")
            }

        }
    }

    // This function takes  data such as
    // "video api model" , "movie with details model" and maps it all into a MovieWithDetailsModel
    // adn returns it for "Movie details page"
   override suspend fun getMovieDetailsForDetailsPage(movieId: Int): ResponseResult<MovieWithDetailsModel> {
        return withContext(Dispatchers.IO) {
            val movieDetails = async { getMovieDetailsForList(movieId) }
            val video = async { getVideo(movieId) }

            try {

                val movieWithDetails = moviesApiMapper.mapMovieDetailsApiToModel(
                    movieDetails.await(),
                    video.await()
                )
                movieWithDetails?.let {
                    return@let ResponseResult.Success(it)
                } ?: ResponseResult.Failure(message = "The Movie details object is null")
            } catch (e: IOException) {
                e.printStackTrace()
                ResponseResult.Failure(message = "Error!! Please check internet connection")
            }

        }
    }



}