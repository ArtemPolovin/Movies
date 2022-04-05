package com.example.movies.ui.search_movie_by_name

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.data.datasource.MoviesPagingSource
import com.example.domain.models.MovieModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class SearchMovieByNameViewModel @Inject constructor(
    private val moviesPagingSource: MoviesPagingSource,
    private val state: SavedStateHandle
): ViewModel() {

    init {
        fetchMoviesByName()
    }

    private var _movies: Flow<PagingData<MovieModel>>? = null
    val movies: Flow<PagingData<MovieModel>>? get() = _movies

    private var lastFetchedMovieResult: Flow<PagingData<MovieModel>>? = null

  private  fun fetchMoviesByName(){

      state.get<String>("movieName")?.let { moviesPagingSource.setupMovieName(it) }

        val lastMovieResult = lastFetchedMovieResult
        if(lastMovieResult != null) _movies = lastMovieResult

        val newFetchedMoviesResult = Pager(config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
            pagingSourceFactory = { moviesPagingSource }
        ).flow.cachedIn(viewModelScope)

        lastFetchedMovieResult = newFetchedMoviesResult

        _movies = newFetchedMoviesResult
    }

}