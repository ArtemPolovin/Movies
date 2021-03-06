package com.sacramento.movies.ui.move_details

import androidx.lifecycle.*
import com.sacramento.data.cache.WatchListChanges
import com.sacramento.domain.models.*
import com.sacramento.domain.usecases.auth.LoadSessionIdUseCase
import com.sacramento.domain.usecases.movie_usecase.*
import com.sacramento.domain.utils.ResponseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getSimilarMoviesUseCase: GetSimilarMoviesUseCase,
    private val getRecommendationsMoviesUseCase: GetRecommendationsMoviesUseCase,
    private val loadSessionIdUseCase: LoadSessionIdUseCase,
    private val saveOrDeleteMovieFromWatchListUseCase: SaveOrDeleteMovieFromWatchListUseCase,
    private val getMovieAccountStateUseCase: GetMovieAccountStateUseCase,
    private val watchListChanges: WatchListChanges,
    private val getTrailerListUseCase: GetTrailerListUseCase
) : ViewModel() {

    private val _movieDetailsModel = MutableLiveData<ResponseResult<MovieWithDetailsModel>>()
    val movieDetailsModel: LiveData<ResponseResult<MovieWithDetailsModel>>
        get() = _movieDetailsModel

    private val _similarMovies = MutableLiveData<ResponseResult<List<MovieModel>>>()
    val similarMovies: LiveData<ResponseResult<List<MovieModel>>> get() = _similarMovies

    private val _recommendationsMovies = MutableLiveData<ResponseResult<List<MovieModel>>>()
    val recommendationsMovies: LiveData<ResponseResult<List<MovieModel>>> get() = _recommendationsMovies

    private val _movieAccountState = MutableLiveData< ResponseResult<MovieAccountStateModel>>()
    val movieAccountState: LiveData< ResponseResult<MovieAccountStateModel>> get() = _movieAccountState

    private val _trailerList = MutableLiveData<ResponseResult<List<TrailerModel>>>()
    val trailerList: LiveData<ResponseResult<List<TrailerModel>>> get() = _trailerList

    fun saveOrDeleteMovieFromWatchList(saveToWatchListModel: SaveToWatchListModel) {
        viewModelScope.launch {
            saveOrDeleteMovieFromWatchListUseCase.execute(saveToWatchListModel, loadSessionIdUseCase.execute())
        }
    }

    fun fetchMovieDetails(movieId: Int) {
        _movieDetailsModel.value = ResponseResult.Loading
        viewModelScope.launch {
            _movieDetailsModel.value = getMovieDetailsUseCase.execute(movieId)
            _similarMovies.value = getSimilarMoviesUseCase.execute(movieId)
            _recommendationsMovies.value = getRecommendationsMoviesUseCase.execute(movieId)

        }
    }

     fun fetchTrailersFromNetwork(movieId: Int) {
        _trailerList.value = ResponseResult.Loading

        viewModelScope.launch {
            _trailerList.value = getTrailerListUseCase.execute(movieId)
        }
    }


    fun getMovieAccountState(movieId: Int) {
        viewModelScope.launch {
            _movieAccountState.value = getMovieAccountStateUseCase.execute(loadSessionIdUseCase.execute(), movieId)
        }
    }

    fun saveWatchListChangesState() {
        watchListChanges.saveIsWatchListChanged(true)
    }

}