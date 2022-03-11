package com.example.movies.ui.search_movie_by_name

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

@HiltViewModel
class SearchMovieByNameViewModel @Inject constructor(
    private val moviesPagingSource: MoviesPagingSource
): ViewModel() {

    private var lastFetchedMovieResult: Flow<PagingData<MovieModel>>? = null

    fun fetchMoviesByName(movieName: String): Flow<PagingData<MovieModel>> {
        moviesPagingSource.setupMovieName(movieName)

        val lastMovieResult = lastFetchedMovieResult
        if(lastMovieResult != null) return lastMovieResult

        val newFetchedMoviesResult = Pager(config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
            pagingSourceFactory = { moviesPagingSource }
        ).flow.cachedIn(viewModelScope)

        lastFetchedMovieResult = newFetchedMoviesResult

        return newFetchedMoviesResult
    }
}