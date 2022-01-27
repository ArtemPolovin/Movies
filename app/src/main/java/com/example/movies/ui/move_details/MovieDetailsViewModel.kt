package com.example.movies.ui.move_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.MovieModel
import com.example.domain.models.MovieWithDetailsModel
import com.example.domain.usecases.movie_usecase.GetMovieDetailsUseCase
import com.example.domain.usecases.movie_usecase.GetRecommendationsMoviesUseCase
import com.example.domain.usecases.movie_usecase.GetSimilarMoviesUseCase
import com.example.domain.usecases.movie_usecase.InsertMovieToDbUseCase
import com.example.domain.utils.ResponseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val insertMovieToDbUseCase: InsertMovieToDbUseCase,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getSimilarMoviesUseCase: GetSimilarMoviesUseCase,
    private val getRecommendationsMoviesUseCase: GetRecommendationsMoviesUseCase
): ViewModel(){

    private val _movieDetailsModel = MutableLiveData<ResponseResult<MovieWithDetailsModel>>()
    val movieDetailsModel: LiveData<ResponseResult<MovieWithDetailsModel>>
        get()= _movieDetailsModel

    private val _similarMovies = MutableLiveData<ResponseResult<List<MovieModel>>>()
    val similarMovies: LiveData<ResponseResult<List<MovieModel>>>get() = _similarMovies

    private val _recommendationsMovies = MutableLiveData<ResponseResult<List<MovieModel>>>()
    val recommendationsMovies: LiveData<ResponseResult<List<MovieModel>>>get() = _recommendationsMovies

    fun insertMovieToDb(movie: MovieWithDetailsModel) {
        viewModelScope.launch { insertMovieToDbUseCase(movie) }
    }

    fun fetchMovieDetails(movieId: Int) {
        _movieDetailsModel.value =  ResponseResult.Loading
        viewModelScope.launch {
            _movieDetailsModel.value = getMovieDetailsUseCase.execute(movieId)
            _similarMovies.value = getSimilarMoviesUseCase.execute(movieId)
            _recommendationsMovies.value = getRecommendationsMoviesUseCase.execute(movieId)

        }
    }

}