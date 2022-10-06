package com.sacramento.movies.ui.movies

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sacramento.data.datasource.MoviesWithDetailsPagingSource
import com.sacramento.data.datasource.MoviesWithDetailsPagingSourceDB
import com.sacramento.data.utils.ConnectionHelper
import com.sacramento.domain.models.MovieWithDetailsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val moviesWithDetailsPagingSource: MoviesWithDetailsPagingSource,
    private val moviesWithDetailsPagingSourceDB: MoviesWithDetailsPagingSourceDB,
    private val connectionHelper: ConnectionHelper
) : ViewModel() {

    private var lastFetchedMovieResultFromServer: Flow<PagingData<MovieWithDetailsModel>>? = null
    private var lastFetchedMovieResultFromDB: Flow<PagingData<MovieWithDetailsModel>>? = null

    fun getMovies(): Flow<PagingData<MovieWithDetailsModel>> {
        return if (connectionHelper.isNetworkAvailable()) {
            fetchMoviesWithDetailsFromService()
            //fetchMoviesWithDetailsFromDB()
        } else{
            fetchMoviesWithDetailsFromDB()
        }
    }

    private fun fetchMoviesWithDetailsFromService(): Flow<PagingData<MovieWithDetailsModel>> {
        val lastMovieResult = lastFetchedMovieResultFromServer
        if (lastMovieResult != null) return lastMovieResult

        val newFetchedMovieResult = Pager(config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
            pagingSourceFactory = { moviesWithDetailsPagingSource }
        ).flow.cachedIn(viewModelScope)

        lastFetchedMovieResultFromServer = newFetchedMovieResult

        return newFetchedMovieResult
    }

    private fun fetchMoviesWithDetailsFromDB() :Flow<PagingData<MovieWithDetailsModel>>{
        val lastMovieResult = lastFetchedMovieResultFromDB
        if (lastMovieResult != null) return lastMovieResult

        val newFetchedMovieResult = Pager(config = PagingConfig(
            pageSize = 100,
            enablePlaceholders = false,
            initialLoadSize = 100
        ),
            pagingSourceFactory = {moviesWithDetailsPagingSourceDB}
        ).flow.cachedIn(viewModelScope)

        lastFetchedMovieResultFromDB = newFetchedMovieResult

        return newFetchedMovieResult
    }

    fun clearLastFetchedData() {
        lastFetchedMovieResultFromDB = null
        lastFetchedMovieResultFromServer = null
    }

}
