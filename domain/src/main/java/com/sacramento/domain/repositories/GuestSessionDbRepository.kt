package com.sacramento.domain.repositories

import com.sacramento.domain.models.MovieModel
import com.sacramento.domain.models.MovieWithDetailsModel
import kotlinx.coroutines.flow.Flow


interface GuestSessionDbRepository {
    suspend fun insertGuestMovie(movieModel: MovieWithDetailsModel)
    suspend fun getAllGuestMovies(limit: Int, offset: Int): List<MovieModel>
    suspend fun deleteGuestMovie(movieId: Int)
    suspend fun isMovieSaved(movieId: Int): Boolean
    suspend fun deleteListOfGuestWatchList(movieIdList: List<Int>)
}