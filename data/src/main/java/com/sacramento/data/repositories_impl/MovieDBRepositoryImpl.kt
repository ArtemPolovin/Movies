package com.sacramento.data.repositories_impl

import com.sacramento.data.apimodels.movie_details.MovieDetailsModelApi
import com.sacramento.data.cache.SettingsDataCache
import com.sacramento.data.db.dao.MoviesDao
import com.sacramento.data.mapers.MoviesEntityMapper
import com.sacramento.data.utils.DEFAULT_ENGLISH_LANGUAGE_VALUE
import com.sacramento.data.utils.LIMIT_ITEMS_PER_REQUEST
import com.sacramento.data.utils.MONTH
import com.sacramento.data.utils.YEAR
import com.sacramento.domain.models.ApiModel
import com.sacramento.domain.models.MovieModel
import com.sacramento.domain.models.MovieWithDetailsModel
import com.sacramento.domain.models.MoviesSortedByGenreContainerModel
import com.sacramento.domain.repositories.MovieCategoriesRepository
import com.sacramento.domain.repositories.MovieDBRepository
import com.sacramento.domain.utils.ResponseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MovieDBRepositoryImpl(
    private val moviesDao: MoviesDao,
    private val movieEntityMapper: MoviesEntityMapper,
    private val settingsDataCache: SettingsDataCache,
    private val movieCategoriesRepository: MovieCategoriesRepository
) : MovieDBRepository {

    private val curYear = SimpleDateFormat(YEAR, Locale.getDefault()).format(Date())
    private val curMonth = SimpleDateFormat(MONTH, Locale.getDefault()).format(Date())

    override suspend fun insertMoviesToDB(movieApi: ApiModel) {
        moviesDao.run {
            insertMovie(movieEntityMapper.mapMovieModelToEntity(movieApi as MovieDetailsModelApi))
            insertMovieGenreCrossRef(
                movieEntityMapper.mapMovieApiToMovieGenreCrossRefEntity(
                    movieApi
                )
            )
        }
    }

    override suspend fun getMoviesFromDBSortedByPopularity(): List<MovieWithDetailsModel> {
        val curLanguage = settingsDataCache.getLanguage() ?: DEFAULT_ENGLISH_LANGUAGE_VALUE
        val movies = moviesDao.getMovieSortedByPopularity(curLanguage)
        return movieEntityMapper.mapMovieEntityListToMovieWithDetailsModelList(movies)
    }

    override suspend fun getTopRatedMovies(): List<MovieWithDetailsModel> {
        val curLanguage = settingsDataCache.getLanguage() ?: DEFAULT_ENGLISH_LANGUAGE_VALUE
        val movies = moviesDao.getTopRatedMovies(curLanguage)
        return movieEntityMapper.mapMovieEntityListToMovieWithDetailsModelList(movies)
    }

    override suspend fun getUpcomingMovies(): List<MovieWithDetailsModel> {
        val curLanguage = settingsDataCache.getLanguage() ?: DEFAULT_ENGLISH_LANGUAGE_VALUE

        val movies = moviesDao.getUpcomingMovies(curYear, curMonth, curLanguage)
        return movieEntityMapper.mapMovieEntityListToMovieWithDetailsModelList(movies)
    }

    override suspend fun getMoviesByNameFromDB(
        movieName: String,
        limit: Int,
        offset: Int
    ): List<MovieModel> {
        val movies = moviesDao.getMoviesByName(movieName, limit, offset)
        return movieEntityMapper.mapMovieEntityListToMovieModelList(movies)
    }

    override suspend fun getFilteredMovies(
        genreId: String?,
        year: String?,
        rating: Float?,
        sortedByPopularity: String?,
        limit: Int
    ): List<MovieWithDetailsModel> {
        val curLanguage = settingsDataCache.getLanguage() ?: DEFAULT_ENGLISH_LANGUAGE_VALUE
        val movies = moviesDao.getFilteredMovies(
            genreId = genreId,
            language = curLanguage,
            limit = limit,
            sortedByPopularity = sortedByPopularity,
            rating = rating,
            year = year
        )
        return movieEntityMapper.mapMovieEntityListToMovieWithDetailsModelList(movies)
    }

    override suspend fun getMovieById(movieId: Int): ResponseResult<MovieWithDetailsModel> {
        return try {
            val curLanguage = settingsDataCache.getLanguage() ?: DEFAULT_ENGLISH_LANGUAGE_VALUE
            val movieFromDb = moviesDao.getMovieById(movieId, curLanguage)
            if (movieFromDb.isEmpty()) throw IOException("Response from DB was not successful")
            ResponseResult.Success(movieEntityMapper.mapMovieEntityToMovieModel(movieFromDb))
        } catch (e: IOException) {
            e.printStackTrace()
            ResponseResult.Failure(message = "Movie not found")
        }
    }

    override suspend fun getSimilarMovies(movieId: Int): ResponseResult<List<MovieModel>> {
        return try {
            val curLanguage = settingsDataCache.getLanguage() ?: DEFAULT_ENGLISH_LANGUAGE_VALUE
            val movieFromDb = moviesDao.getMovieById(movieId, curLanguage)
            val genreName = movieFromDb.values.first().first().genreId
            val moviesByGenres = moviesDao.getMoviesByGenre(
                genreId = genreName,
                language = curLanguage,
                limit = LIMIT_ITEMS_PER_REQUEST
            )
            ResponseResult.Success(
                movieEntityMapper.mapMovieEntityListToMovieModelList(
                    moviesByGenres
                )
            )
        } catch (e: IOException) {
            e.printStackTrace()
            ResponseResult.Failure(message = "Response from DB was not successful!\nTry again")
        }
    }

    override suspend fun getRecommendationsMovies(movieId: Int): ResponseResult<List<MovieModel>> {
        return try {
            val curLanguage = settingsDataCache.getLanguage() ?: DEFAULT_ENGLISH_LANGUAGE_VALUE
            val movieFromDb = moviesDao.getMovieById(movieId, curLanguage)
            val genreName = movieFromDb.values.first().first().genreId
            val moviesByGenres = moviesDao.getMoviesByGenre(
                genreId = genreName,
                language = curLanguage,
                limit = LIMIT_ITEMS_PER_REQUEST,
                offset = LIMIT_ITEMS_PER_REQUEST
            )
            ResponseResult.Success(
                movieEntityMapper.mapMovieEntityListToMovieModelList(
                    moviesByGenres
                )
            )
        } catch (e: IOException) {
            e.printStackTrace()
            ResponseResult.Failure(message = "Response from DB was not successful!\nTry again")
        }
    }

    override suspend fun insertAllSavedMoviesFromAccount(savedMovies: List<MovieModel>) {
        moviesDao.insertAllSavedMovies(
            movieEntityMapper.mapMovieModelListToSavedMoviesEntityList(
                savedMovies
            )
        )
    }

    override suspend fun insertSavedMovie(savedMovie: MovieModel) {
        moviesDao.insertSavedMovie(movieEntityMapper.mapMovieModelToSavedMovieEntity(savedMovie))
    }

    override suspend fun removeSavedMovieById(movieId: String) {
        moviesDao.removeSavedMovieById(movieId)
    }

    override suspend fun getSavedMovies(limit: Int, offset: Int): List<MovieModel> {
        return movieEntityMapper.mapSavedMoviesEntityToMovieModelList(
            moviesDao.getSavedMovies(
                limit = limit,
                offset = offset
            )
        )
    }

    override suspend fun clearSavedMoviesDB() {
        moviesDao.deleteAllMovies()
    }


    override suspend fun getMoviesSortedByGenreFromDB(): ResponseResult<List<MoviesSortedByGenreContainerModel>> {
        val list = mutableListOf<MoviesSortedByGenreContainerModel>()
        return withContext(Dispatchers.IO) {
            val curLanguage = settingsDataCache.getLanguage() ?: DEFAULT_ENGLISH_LANGUAGE_VALUE
            val genreList = movieCategoriesRepository.getGenres()
            val runningTask = genreList.map { genre ->
                async {
                    val movies = moviesDao.getMoviesByGenre(
                        genreId = genre.id,
                        language = curLanguage,
                        limit = LIMIT_ITEMS_PER_REQUEST
                    )
                    MoviesSortedByGenreContainerModel(
                        genre.id,
                        genre.name,
                        movieEntityMapper.mapMovieEntityListToMovieModelList(movies)
                    )
                }
            }
            list.addAll(runningTask.awaitAll())
            ResponseResult.Success(list)
        }
    }


}