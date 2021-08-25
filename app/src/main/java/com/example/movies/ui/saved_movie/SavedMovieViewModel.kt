package com.example.movies.ui.saved_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.PopularMovieWithDetailsModel
import com.example.domain.models.utils.ResponseResult
import com.example.domain.usecases.movie_usecase.DeleteMovieByIdFromDbUseCase
import com.example.domain.usecases.movie_usecase.GetAllSavedMoviesFromDbUseCase
import com.example.domain.usecases.movie_usecase.InsertMovieToDbUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedMovieViewModel @Inject constructor(
    private val deleteMovieByIdFromDbUseCase: DeleteMovieByIdFromDbUseCase,
    private val getAllSavedMoviesFromDbUseCase: GetAllSavedMoviesFromDbUseCase,
) : ViewModel() {

    private val _savedMoviesList = MutableLiveData< ResponseResult<List<PopularMovieWithDetailsModel>>>()
    val savedMoviesList: LiveData<ResponseResult<List<PopularMovieWithDetailsModel>>> get() = _savedMoviesList

    init {
        fetchSavedMoviesFromDb()
    }


    private fun fetchSavedMoviesFromDb() {
        viewModelScope.launch {
            getAllSavedMoviesFromDbUseCase().collect {
                _savedMoviesList.value = ResponseResult.Loading
                _savedMoviesList.value = it
            }
        }
    }

    fun deleteMovieByIdFromDb(movieId: List<Int>) {
        viewModelScope.launch { deleteMovieByIdFromDbUseCase(movieId) }
    }

    fun refreshSaveMoviesList() {
        fetchSavedMoviesFromDb()
    }



}