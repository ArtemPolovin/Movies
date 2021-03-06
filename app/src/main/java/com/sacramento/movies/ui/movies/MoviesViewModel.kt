package com.sacramento.movies.ui.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sacramento.data.datasource.MoviesWithDetailsPagingSource
import com.sacramento.domain.models.MovieWithDetailsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val moviesWithDetailsPagingSource: MoviesWithDetailsPagingSource,
) : ViewModel() {


    private var lastFetchedMovieResult: Flow<PagingData<MovieWithDetailsModel>>? = null

    fun fetchPopularMoviesWithDetails(): Flow<PagingData<MovieWithDetailsModel>> {

        val lastMovieResult = lastFetchedMovieResult
        if (lastMovieResult != null) return lastMovieResult

        val newFetchedMovieResult = Pager(config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
            pagingSourceFactory = { moviesWithDetailsPagingSource }
        ).flow.cachedIn(viewModelScope)

        lastFetchedMovieResult = newFetchedMovieResult

        return newFetchedMovieResult
    }

}