package com.example.movies.ui.saved_movie

import androidx.lifecycle.*
import com.example.data.cache.WatchListChanges
import com.example.domain.models.MovieModel
import com.example.domain.models.SaveToWatchListModel
import com.example.domain.usecases.auth.LoadSessionIdUseCase
import com.example.domain.usecases.movie_usecase.GetWatchListUseCase
import com.example.domain.usecases.movie_usecase.SaveOrDeleteMovieFromWatchListUseCase
import com.example.domain.utils.ResponseResult
import com.example.movies.utils.DELETE_MOVIE
import com.example.movies.utils.MEDIA_TYPE_MOVIE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class SavedMovieViewModel @Inject constructor(
    private val getWatchListUseCase: GetWatchListUseCase,
    private val loadSessionIdUseCase: LoadSessionIdUseCase,
    private val saveOrDeleteMovieFromWatchListUseCase: SaveOrDeleteMovieFromWatchListUseCase,
    private val watchListChanges: WatchListChanges
) : ViewModel() {

    private val _watchList = MutableLiveData<ResponseResult<List<MovieModel>>>()
    val watchList: LiveData<ResponseResult<List<MovieModel>>> get() = _watchList

    init {
        fetchWatchList()
    }

   private fun fetchWatchList() {
         _watchList.value = ResponseResult.Loading

       val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
           _watchList.value = ResponseResult.Failure(
               message = "Unknown error has occurred. Please check internet connection"
           )
       }
        viewModelScope.launch(handler) {
            _watchList.value = getWatchListUseCase.execute(loadSessionIdUseCase.execute())
        }
    }

    private suspend fun saveOrDeleteMovieFromWatchList(saveToWatchListModel: SaveToWatchListModel) {
        saveOrDeleteMovieFromWatchListUseCase.execute(
            saveToWatchListModel,
            loadSessionIdUseCase.execute()
        )
    }

     fun deleteMoviesFromWatchList(moviesIdList: List<Int>) {
        viewModelScope.launch {
           val b =  launch {
                moviesIdList.forEach { movieId ->
                   saveOrDeleteMovieFromWatchList(
                        SaveToWatchListModel(MEDIA_TYPE_MOVIE, movieId, DELETE_MOVIE)
                    )
                }
            }
            b.join()
            fetchWatchList()
        }
    }

    fun refreshIfWatchListWasChanged() {
        if (watchListChanges.loadIsWatchListChanged()) {
            fetchWatchList()
            watchListChanges.saveIsWatchListChanged(false)
        }
    }

}