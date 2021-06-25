package com.example.movies.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.data.datasource.MoviePagingSource
import com.example.domain.models.PopularMovieWithDetailsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val moviePagingSource: MoviePagingSource
) : ViewModel() {

    private var lastFetchedMovieResult: Flow<PagingData<PopularMovieWithDetailsModel>>? = null

    fun fetchPopularMoviesWithDetails(): Flow<PagingData<PopularMovieWithDetailsModel>> {

        val lastMovieResult = lastFetchedMovieResult
        if (lastMovieResult != null) return lastMovieResult

        val newFetchedMovieResult = Pager(config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
            pagingSourceFactory = { moviePagingSource }
        ).flow.cachedIn(viewModelScope)

        lastFetchedMovieResult = newFetchedMovieResult

        return newFetchedMovieResult
    }

}