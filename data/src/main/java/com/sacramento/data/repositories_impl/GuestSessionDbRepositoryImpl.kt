package com.sacramento.data.repositories_impl

import com.sacramento.data.db.dao.GuestSessionDao
import com.sacramento.data.mapers.GuestSessionMapper
import com.sacramento.domain.models.MovieModel
import com.sacramento.domain.models.MovieWithDetailsModel
import com.sacramento.domain.repositories.GuestSessionDbRepository
import java.io.IOException

class GuestSessionDbRepositoryImpl(
    private val guestDao: GuestSessionDao,
    private val guestSessionMapper: GuestSessionMapper
) : GuestSessionDbRepository {
    override suspend fun insertGuestMovie(movieModel: MovieWithDetailsModel) {
        guestDao.insertGuestMovie(guestSessionMapper.mapMovieModelToEntity(movieModel))
    }

    override suspend fun getAllGuestMovies(limit: Int, offset: Int): List<MovieModel> {
        return guestSessionMapper.mapGuestSavedMovieEntityToMovieModelList(
            guestDao.getAllGuestSavedMovies(
                limit = limit,
                offset = offset
            )
        )
    }

    override suspend fun deleteGuestMovie(movieId: Int) {
        guestDao.deleteGuestMovie(movieId)
    }

    override suspend fun isMovieSaved(movieId: Int): Boolean {
        return try {
            val result = guestDao.getSavedMovieById(movieId)
            result != null
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun deleteListOfGuestWatchList(movieIdList: List<Int>) {
        guestDao.deleteListOfGuestWatchList(movieIdList)
    }
}