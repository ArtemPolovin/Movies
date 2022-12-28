package com.sacramento.movies.ui.search_movie_by_name

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sacramento.data.datasource.MoviesPagingSource
import com.sacramento.data.datasource.MoviesPagingSourceDB
import com.sacramento.data.utils.ConnectionHelper
import com.sacramento.domain.models.MovieModel
import com.sacramento.domain.models.MovieWithDetailsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SearchMovieByNameViewModel @Inject constructor(
    private val moviesPagingSource: MoviesPagingSource,
    private val moviesPagingSourceDB: MoviesPagingSourceDB,
    private val state: SavedStateHandle,
    private val connectionHelper: ConnectionHelper
): ViewModel() {

    private var lastFetchedMovieResult: Flow<PagingData<MovieModel>>? = null
    private var lastFetchedMovieResultDB: Flow<PagingData<MovieModel>>? = null

    fun getMovies(): Flow<PagingData<MovieModel>>{
        return if (connectionHelper.isNetworkAvailable()) {
            fetchMoviesByName()
        } else{
            fetchMoviesByNameFormDB()
        }
    }

  private  fun fetchMoviesByName() : Flow<PagingData<MovieModel>>{
      state.get<String>("movieName")?.let { moviesPagingSource.setupMovieName(it)}

        val lastMovieResult = lastFetchedMovieResult
        if(lastMovieResult != null) return lastMovieResult

        val newFetchedMoviesResult = Pager(config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
            pagingSourceFactory = { moviesPagingSource }
        ).flow.cachedIn(viewModelScope)

        lastFetchedMovieResult = newFetchedMoviesResult

        return newFetchedMoviesResult
    }

    private  fun fetchMoviesByNameFormDB(): Flow<PagingData<MovieModel>>{

        state.get<String>("movieName")?.let { moviesPagingSourceDB.setupMovieName(it) }

        val lastMovieResult = lastFetchedMovieResultDB
        if(lastMovieResult != null) return lastMovieResult


        val newFetchedMoviesResult = Pager(config = PagingConfig(
            pageSize = 100,
            enablePlaceholders = false,
            initialLoadSize = 100
        ),
            pagingSourceFactory = { moviesPagingSourceDB }
        ).flow.cachedIn(viewModelScope)

        lastFetchedMovieResultDB = newFetchedMoviesResult

        return newFetchedMoviesResult
    }

    fun clearLastFetchedData() {
        lastFetchedMovieResult = null
        lastFetchedMovieResultDB = null
    }

}