package com.example.movies.ui.move_details

import androidx.lifecycle.*
import com.example.data.cache.SessionIdDataCache
import com.example.data.cache.WatchListChanges
import com.example.domain.models.MovieAccountStateModel
import com.example.domain.models.MovieModel
import com.example.domain.models.MovieWithDetailsModel
import com.example.domain.models.SaveToWatchListModel
import com.example.domain.usecases.movie_usecase.*
import com.example.domain.utils.ResponseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val insertMovieToDbUseCase: InsertMovieToDbUseCase,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getSimilarMoviesUseCase: GetSimilarMoviesUseCase,
    private val getRecommendationsMoviesUseCase: GetRecommendationsMoviesUseCase,
    private val sessionIdDataCache: SessionIdDataCache,
    private val saveOrDeleteMovieFromWatchListUseCase: SaveOrDeleteMovieFromWatchListUseCase,
    private val getMovieAccountStateUseCase: GetMovieAccountStateUseCase,
    private val watchListChanges: WatchListChanges
) : ViewModel() {

    private val _movieDetailsModel = MutableLiveData<ResponseResult<MovieWithDetailsModel>>()
    val movieDetailsModel: LiveData<ResponseResult<MovieWithDetailsModel>>
        get() = _movieDetailsModel

    private val _similarMovies = MutableLiveData<ResponseResult<List<MovieModel>>>()
    val similarMovies: LiveData<ResponseResult<List<MovieModel>>> get() = _similarMovies

    private val _recommendationsMovies = MutableLiveData<ResponseResult<List<MovieModel>>>()
    val recommendationsMovies: LiveData<ResponseResult<List<MovieModel>>> get() = _recommendationsMovies

    private val _movieAccountState = MutableLiveData<MovieAccountStateModel>()
    val movieAccountState: LiveData<MovieAccountStateModel> get() = _movieAccountState


   /* fun insertMovieToDb(movie: MovieWithDetailsModel) {
        viewModelScope.launch { insertMovieToDbUseCase(movie) }
    }*/

    fun saveOrDeleteMovieFromWatchList(saveToWatchListModel: SaveToWatchListModel) {
        viewModelScope.launch {
            saveOrDeleteMovieFromWatchListUseCase.execute(saveToWatchListModel, sessionIdDataCache.loadSessionId())
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


    fun getMovieAccountState(movieId: Int) {
        viewModelScope.launch {
            val movieAccountResponse =
                getMovieAccountStateUseCase.execute(sessionIdDataCache.loadSessionId(), movieId)
            when (movieAccountResponse) {
                is ResponseResult.Success -> _movieAccountState.value = movieAccountResponse.data
                is ResponseResult.Failure -> throw RuntimeException(movieAccountResponse.message)
            }

        }
    }

    fun saveWatchListChangesState() {
        watchListChanges.saveIsWatchListChanged(true)
    }

}