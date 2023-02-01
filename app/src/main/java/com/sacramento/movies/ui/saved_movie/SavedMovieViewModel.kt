package com.sacramento.movies.ui.saved_movie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sacramento.data.datasource.GuestSessionPagingSource
import com.sacramento.data.datasource.MoviesPagingSource
import com.sacramento.data.datasource.MoviesPagingSourceDB
import com.sacramento.data.utils.ConnectionHelper
import com.sacramento.domain.models.MovieModel
import com.sacramento.domain.models.SaveToWatchListModel
import com.sacramento.domain.usecases.auth.LoadSessionIdUseCase
import com.sacramento.domain.usecases.movie_usecase.CleanSavedMoviesDbUseCase
import com.sacramento.domain.usecases.movie_usecase.SaveOrDeleteMovieFromWatchListUseCase
import com.sacramento.domain.usecases.movie_usecase.guest_session.DeleteListOfGuestWatchListUseCase
import com.sacramento.movies.utils.DELETE_MOVIE
import com.sacramento.movies.utils.MEDIA_TYPE_MOVIE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedMovieViewModel @Inject constructor(
    private val loadSessionIdUseCase: LoadSessionIdUseCase,
    private val saveOrDeleteMovieFromWatchListUseCase: SaveOrDeleteMovieFromWatchListUseCase,
    private val connectionHelper: ConnectionHelper,
    private val moviesPagingSource: MoviesPagingSource,
    private val moviesPagingSourceDB: MoviesPagingSourceDB,
    private val cleanSavedMoviesDbUseCase: CleanSavedMoviesDbUseCase,
    private val guestSessionPagingSource: GuestSessionPagingSource,
    private val deleteListOfGuestWatchListUseCase: DeleteListOfGuestWatchListUseCase
) : ViewModel() {

    private val _isSelectedMoviesDeleted = MutableLiveData<Boolean>().apply { value = false }
    val isSelectedMoviesDeleted: MutableLiveData<Boolean> get() = _isSelectedMoviesDeleted


    private var lastFetchedMovieResult: Flow<PagingData<MovieModel>>? = null
    private var lastFetchedMovieResultDB: Flow<PagingData<MovieModel>>? = null
    private var lastFetchedMovieResultGuest: Flow<PagingData<MovieModel>>? = null

    private fun isUserLoggedIn(): Boolean {
        return loadSessionIdUseCase.execute().isNotBlank()
    }

    fun isNetworkAvailable(): Boolean {
        return connectionHelper.isNetworkAvailable()
    }

    fun getSavedMovies(): Flow<PagingData<MovieModel>> {
        return if (isNetworkAvailable()) {
            if (isUserLoggedIn()) fetchWatchListFromApi()
            else fetchGuestWatchListFromDb()
        } else {
            fetchWatchListFromDB()
        }
    }


    private fun fetchWatchListFromApi(): Flow<PagingData<MovieModel>> {
        val lastMovieResult = lastFetchedMovieResult
        if (lastMovieResult != null) return lastMovieResult

        val newFetchedMoviesResult = Pager(config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
            pagingSourceFactory = { moviesPagingSource }
        ).flow.cachedIn(viewModelScope)

        lastFetchedMovieResult = newFetchedMoviesResult

        return newFetchedMoviesResult
    }

    private fun fetchWatchListFromDB(): Flow<PagingData<MovieModel>> {
        val lastMovieResult = lastFetchedMovieResultDB
        if (lastMovieResult != null) return lastMovieResult

        val newFetchedMoviesResult = Pager(config = PagingConfig(
            pageSize = 30,
            enablePlaceholders = false
        ),
            pagingSourceFactory = { moviesPagingSourceDB }
        ).flow.cachedIn(viewModelScope)

        lastFetchedMovieResultDB = newFetchedMoviesResult

        return newFetchedMoviesResult
    }

    private fun fetchGuestWatchListFromDb(): Flow<PagingData<MovieModel>> {
        val lastMovieResult = lastFetchedMovieResultGuest
        if (lastMovieResult != null) return lastMovieResult

        val newFetchedMoviesResult = Pager(config = PagingConfig(
            pageSize = 30,
            enablePlaceholders = false
        ),
            pagingSourceFactory = { guestSessionPagingSource }
        ).flow.cachedIn(viewModelScope)

        lastFetchedMovieResultGuest = newFetchedMoviesResult

        return newFetchedMoviesResult
    }

    private suspend fun saveOrDeleteMovieFromWatchList(saveToWatchListModel: SaveToWatchListModel) {
        saveOrDeleteMovieFromWatchListUseCase.execute(
            saveToWatchListModel,
            loadSessionIdUseCase.execute()
        )
    }

    fun deleteMoviesFromWatchList(moviesIdList: List<Int>) {
        if (isUserLoggedIn()) deleteMovieFromWatchListAccount(moviesIdList)
        else deleteMoviesFromGuestWatchListDb(moviesIdList)
    }

    private fun deleteMovieFromWatchListAccount(moviesIdList: List<Int>) {
        viewModelScope.launch {
            cleanSavedMoviesDbUseCase.execute()
            val b = launch {
                moviesIdList.forEach { movieId ->
                    saveOrDeleteMovieFromWatchList(
                        SaveToWatchListModel(MEDIA_TYPE_MOVIE, movieId, DELETE_MOVIE)
                    )
                }
            }
            b.join()
            _isSelectedMoviesDeleted.value = true
        }
    }

    private fun deleteMoviesFromGuestWatchListDb(movieIdList: List<Int>) {
        viewModelScope.launch {
            launch { deleteListOfGuestWatchListUseCase.execute(movieIdList) }.join()
            _isSelectedMoviesDeleted.value = true
        }
    }

    fun clearLastFetchedMovieResult() {
        lastFetchedMovieResult = null
        lastFetchedMovieResultGuest = null
    }

}