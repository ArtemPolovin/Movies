package com.sacramento.movies.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sacramento.data.utils.ConnectionHelper
import com.sacramento.domain.models.MoviePosterViewPagerModel
import com.sacramento.domain.models.MoviesSortedByGenreContainerModel
import com.sacramento.domain.usecases.movie_usecase.GetMoviesSortedByGenreFromDbUseCase
import com.sacramento.domain.usecases.movie_usecase.GetMoviesSortedByGenreUseCase
import com.sacramento.domain.usecases.movie_usecase.GetUpComingMoviesFromDbUseCase
import com.sacramento.domain.usecases.movie_usecase.GetUpComingMoviesUseCase
import com.sacramento.domain.utils.ResponseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMoviesSortedByGenreUseCase: GetMoviesSortedByGenreUseCase,
    private val getUpcomingMoviesUseCase: GetUpComingMoviesUseCase,
    private val getMoviesSortedByGenreFromDbUseCase: GetMoviesSortedByGenreFromDbUseCase,
    private val getUpComingMoviesFromDbUseCase: GetUpComingMoviesFromDbUseCase,
    private val connectionHelper: ConnectionHelper,
) : ViewModel() {

    private val _sortedMoviesByGenre =
        MutableLiveData<ResponseResult<List<MoviesSortedByGenreContainerModel>>>()
    val sortedMoviesByGenreModel: LiveData<ResponseResult<List<MoviesSortedByGenreContainerModel>>> get() = _sortedMoviesByGenre

    private val _upcomingMovies = MutableLiveData<List<MoviePosterViewPagerModel>>()
    val upcomingMovies: LiveData<List<MoviePosterViewPagerModel>> get() = _upcomingMovies

    val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        throwable.printStackTrace()
        _sortedMoviesByGenre.value = ResponseResult.Failure(
            message = "Unknown error has occurred. Please check internet connection"
        )
    }


    init {
        refreshData()
    }

    private fun fetchSortedMoviesByGenreFromApi() {
        viewModelScope.launch(handler) {

            val sortedMovies = async { getMoviesSortedByGenreUseCase.execute() }
            val upComingMovies = async { getUpcomingMoviesUseCase.execute(1) }

            _sortedMoviesByGenre.value = sortedMovies.await()
            _upcomingMovies.value = upComingMovies.await()
        }
    }

    private fun fetchSortedMoviesByGenreFromDB() {
        viewModelScope.launch(handler) {

            val sortedMovies = async { getMoviesSortedByGenreFromDbUseCase.execute() }
            val upComingMovies = async { getUpComingMoviesFromDbUseCase.execute() }

            _sortedMoviesByGenre.value = sortedMovies.await()
            _upcomingMovies.value = upComingMovies.await()
        }
    }


    fun refreshData() {
        _sortedMoviesByGenre.value = ResponseResult.Loading
        if (connectionHelper.isNetworkAvailable()) {
            fetchSortedMoviesByGenreFromApi()
        }else{
            fetchSortedMoviesByGenreFromDB()
        }

    }


}