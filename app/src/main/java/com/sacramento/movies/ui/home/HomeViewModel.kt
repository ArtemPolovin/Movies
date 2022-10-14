package com.sacramento.movies.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sacramento.data.db.dao.MoviesDao
import com.sacramento.domain.models.MoviePosterViewPagerModel
import com.sacramento.domain.models.MoviesSortedByGenreContainerModel
import com.sacramento.domain.usecases.movie_usecase.GetMoviesSortedByGenreUseCase
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
) : ViewModel() {

    private val _sortedMoviesByGenre =
        MutableLiveData<ResponseResult<List<MoviesSortedByGenreContainerModel>>>()
    val sortedMoviesByGenreModel: LiveData<ResponseResult<List<MoviesSortedByGenreContainerModel>>> get() = _sortedMoviesByGenre

    private val _upcomingMovies = MutableLiveData<List<MoviePosterViewPagerModel>>()
    val upcomingMovies: LiveData<List<MoviePosterViewPagerModel>> get() = _upcomingMovies


    init {
        refreshData()
    }

    private fun fetchSortedMoviesByGenre() {
        _sortedMoviesByGenre.value = ResponseResult.Loading

        val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
            _sortedMoviesByGenre.value = ResponseResult.Failure(
                message = "Unknown error has occurred. Please check internet connection"
            )
        }

        viewModelScope.launch(handler) {

            val sortedMovies = async { getMoviesSortedByGenreUseCase.execute() }
            val upComingMovies = async { getUpcomingMoviesUseCase.execute(1) }

            _sortedMoviesByGenre.value = sortedMovies.await()
            _upcomingMovies.value = upComingMovies.await()
        }
    }

    fun refreshData() {
        fetchSortedMoviesByGenre()
    }


}