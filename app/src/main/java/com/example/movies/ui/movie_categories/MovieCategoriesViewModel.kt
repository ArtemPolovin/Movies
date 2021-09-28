package com.example.movies.ui.movie_categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.MovieCategoriesCellModel
import com.example.domain.usecases.movie_usecase.GetMoviesCategoriesCellsUseCase
import com.example.domain.utils.ResponseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieCategoriesViewModel @Inject constructor(
    private val getMoviesCategoriesCellsUseCase: GetMoviesCategoriesCellsUseCase
) : ViewModel() {

    private val _movieCategoriesCellsList =
        MutableLiveData<ResponseResult<List<MovieCategoriesCellModel>>>()
    val movieCategoriesCellsList: LiveData<ResponseResult<List<MovieCategoriesCellModel>>> get() = _movieCategoriesCellsList

    init {
        fetchMovieCategoriesCellsList()
    }

    private fun fetchMovieCategoriesCellsList() {
        _movieCategoriesCellsList.value = ResponseResult.Loading

        viewModelScope.launch {
            _movieCategoriesCellsList.value = getMoviesCategoriesCellsUseCase()
        }
    }
}