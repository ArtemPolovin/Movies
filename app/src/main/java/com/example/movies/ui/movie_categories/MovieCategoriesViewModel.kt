package com.example.movies.ui.movie_categories

import androidx.lifecycle.ViewModel
import com.example.domain.usecases.movie_usecase.GetMoviesCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieCategoriesViewModel @Inject constructor(
    private val getMoviesCategoriesUseCase: GetMoviesCategoriesUseCase
) : ViewModel() {

    fun fetchMovieCategoriesList() = getMoviesCategoriesUseCase.execute()

}