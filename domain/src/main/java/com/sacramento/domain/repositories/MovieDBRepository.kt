package com.sacramento.domain.repositories

import com.sacramento.domain.models.ApiModel
import com.sacramento.domain.models.MovieModel
import com.sacramento.domain.models.MovieWithDetailsModel

interface  MovieDBRepository {
    suspend fun insertMoviesToDB(movieApi: ApiModel)

    suspend fun getMoviesFromDBSortedByPopularity(): List<MovieWithDetailsModel>

    suspend fun getTopRatedMovies(): List<MovieWithDetailsModel>

    suspend fun getUpcomingMovies(): List<MovieWithDetailsModel>

    suspend fun getMoviesByNameFromDB(movieName: String, limit: Int, offset: Int):  List<MovieModel>

    suspend fun getFilteredMovies(
        genreId: String?,
        year: String?,
        rating: Float?,
        sortedByPopularity: String?,
        limit: Int
    ): List<MovieWithDetailsModel>
}