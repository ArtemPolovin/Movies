package com.example.movies.ui.movie_categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.data.datasource.MoviesPagingSource
import com.example.domain.models.MovieCategoryModel
import com.example.domain.models.MovieModel
import com.example.domain.models.MovieWithDetailsModel
import com.example.domain.usecases.movie_usecase.GetMoviesCategoriesUseCase
import com.example.domain.utils.ResponseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieCategoriesViewModel @Inject constructor(
    private val getMoviesCategoriesUseCase: GetMoviesCategoriesUseCase
) : ViewModel() {


    private val _moviesCategoriesList = MutableLiveData<ResponseResult<List<MovieCategoryModel>>>()
    val moviesCategoriesList: LiveData<ResponseResult<List<MovieCategoryModel>>> get() = _moviesCategoriesList



    init {
        fetchMovieCategoriesList()
    }

    private fun fetchMovieCategoriesList() {
        _moviesCategoriesList.value = ResponseResult.Loading
        viewModelScope.launch {
            _moviesCategoriesList.value = getMoviesCategoriesUseCase.execute()
        }
    }


    fun refreshData() {
        fetchMovieCategoriesList()
    }

}