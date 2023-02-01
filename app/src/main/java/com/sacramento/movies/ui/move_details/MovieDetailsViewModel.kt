package com.sacramento.movies.ui.move_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sacramento.data.cache.WatchListChanges
import com.sacramento.data.utils.ConnectionHelper
import com.sacramento.domain.models.*
import com.sacramento.domain.usecases.auth.LoadSessionIdUseCase
import com.sacramento.domain.usecases.movie_usecase.*
import com.sacramento.domain.usecases.movie_usecase.guest_session.CheckIfGuestMovieIsSavedUseCase
import com.sacramento.domain.usecases.movie_usecase.guest_session.DeleteMovieFromGuestWatchListDbUseCase
import com.sacramento.domain.usecases.movie_usecase.guest_session.InsertGuestSavedMovieToDbUseCase
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
    private val getTrailerListUseCase: GetTrailerListUseCase,
    private val getMovieByIdFromDBUseCase: GetMovieByIdFromDBUseCase,
    private val connectionHelper: ConnectionHelper,
    private val getSimilarMoviesFromDBUseCase: GetSimilarMoviesFromDBUseCase,
    private val getRecommendationsMoviesFromDBUseCase: GetRecommendationsMoviesFromDBUseCase,
    private val insertGuestSavedMovieToDbUseCase: InsertGuestSavedMovieToDbUseCase,
    private val isGuestMovieSavedToDbUseCase: CheckIfGuestMovieIsSavedUseCase,
    private val deleteMovieFromGuestWatchListDbUseCase: DeleteMovieFromGuestWatchListDbUseCase
) : ViewModel() {

    private val _movieDetailsModel = MutableLiveData<ResponseResult<MovieWithDetailsModel>>()
    val movieDetailsModel: LiveData<ResponseResult<MovieWithDetailsModel>>
        get() = _movieDetailsModel

    private val _similarMovies = MutableLiveData<ResponseResult<List<MovieModel>>>()
    val similarMovies: LiveData<ResponseResult<List<MovieModel>>> get() = _similarMovies

    private val _recommendationsMovies = MutableLiveData<ResponseResult<List<MovieModel>>>()
    val recommendationsMovies: LiveData<ResponseResult<List<MovieModel>>> get() = _recommendationsMovies

    private val _movieAccountState = MutableLiveData<ResponseResult<MovieAccountStateModel>>()
    val movieAccountState: LiveData<ResponseResult<MovieAccountStateModel>> get() = _movieAccountState

    private val _trailerList = MutableLiveData<ResponseResult<List<TrailerModel>>>()
    val trailerList: LiveData<ResponseResult<List<TrailerModel>>> get() = _trailerList

    private val _isGuestMovieSaved = MutableLiveData<Boolean>().apply { value = false }
    val isGuestMovieSaved: LiveData<Boolean> get() = _isGuestMovieSaved

    /*    fun saveOrDeleteMovieFromWatchList(saveToWatchListModel: SaveToWatchListModel) {
           viewModelScope.launch {
               saveOrDeleteMovieFromWatchListUseCase.execute(
                   saveToWatchListModel,
                   loadSessionIdUseCase.execute()
               )
           }
       }*/

    fun saveOrDeleteMovieFromWatchList(saveToWatchListModel: SaveToWatchListModel) {
        viewModelScope.launch {
            saveOrDeleteMovieFromWatchListUseCase.execute(
                saveToWatchListModel,
                loadSessionIdUseCase.execute()
            )
        }

    }

    fun getMovieDetails(movieId: Int) {
        _movieDetailsModel.value = ResponseResult.Loading
        if (connectionHelper.isNetworkAvailable()) {
            getMovieDetailsFromNetwork(movieId)
        } else {
            getMovieDetailsFromDb(movieId)
        }
    }

    private fun getMovieDetailsFromNetwork(movieId: Int) {
        viewModelScope.launch {
            _movieDetailsModel.value = getMovieDetailsUseCase.execute(movieId)
            _similarMovies.value = getSimilarMoviesUseCase.execute(movieId)
            _recommendationsMovies.value = getRecommendationsMoviesUseCase.execute(movieId)

        }
    }

    private fun getMovieDetailsFromDb(movieId: Int) {
        viewModelScope.launch {
            _movieDetailsModel.value = getMovieByIdFromDBUseCase.execute(movieId)
            _similarMovies.value = getSimilarMoviesFromDBUseCase.execute(movieId)
            _recommendationsMovies.value = getRecommendationsMoviesFromDBUseCase.execute(movieId)
        }
    }

    fun fetchTrailersFromNetwork(movieId: Int) {
        viewModelScope.launch {
            _trailerList.value = getTrailerListUseCase.execute(movieId)
        }
    }


    fun getMovieAccountState(movieId: Int) {
        if (connectionHelper.isNetworkAvailable()) {
            viewModelScope.launch {
                _movieAccountState.value =
                    getMovieAccountStateUseCase.execute(loadSessionIdUseCase.execute(), movieId)
            }
        }

    }

    /*  fun saveMovie(mediaType: String, movieId: Int, isSavedToWatchList: Boolean) {
          viewModelScope.launch {
              if (loadSessionIdUseCase.execute().isNotBlank()) {
                  saveOrDeleteMovieFromWatchList(
                      SaveToWatchListModel(
                          mediaType,
                          movieId,
                          isSavedToWatchList
                      )
                  )
              } else {
                  insertOrDeleteGuestMovieFromDb(movieId)
              }
          }
      }*/

    fun insertMovieToGuestWatchListDb(movieModel: MovieWithDetailsModel) {
        viewModelScope.launch {
            insertGuestSavedMovieToDbUseCase.execute(movieModel)
        }

    }

    fun deleteMovieFromGuestWatchListDb(movieId: Int) {
        viewModelScope.launch {
            deleteMovieFromGuestWatchListDbUseCase.execute(movieId)
        }
    }

    fun checkIfGuestMovieSaved(movieId: Int) {
        // TODO: CREATE CHECKING IF GUEST MOVIE SAVED TO DB 
        viewModelScope.launch {
            _isGuestMovieSaved.value = isGuestMovieSavedToDbUseCase.execute(movieId)
        }
    }

    fun saveWatchListChangesState() {
        watchListChanges.saveIsWatchListChanged(true)
    }

    fun getInternetConnectionState() = connectionHelper.isNetworkAvailable()

    fun isUserLoggedIn(): Boolean {
        return loadSessionIdUseCase.execute().isNotBlank()
    }

}