package com.example.movies.ui.move_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.MovieWithDetailsModel
import com.example.domain.usecases.movie_usecase.InsertMovieToDbUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val insertMovieToDbUseCase: InsertMovieToDbUseCase
): ViewModel(){

    fun insertMovieToDb(movie: MovieWithDetailsModel) {
        viewModelScope.launch { insertMovieToDbUseCase(movie) }
    }
}