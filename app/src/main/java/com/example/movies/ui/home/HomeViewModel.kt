package com.example.movies.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.data.datasource.MoviePagingSource
import com.example.data.utils.SessionIdDataCache
import com.example.domain.models.LogoutRequestBodyModel
import com.example.domain.models.PopularMovieWithDetailsModel
import com.example.domain.usecases.auth.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val moviePagingSource: MoviePagingSource,
    private val logoutUseCase: LogoutUseCase,
    private val sessionIdDataCache: SessionIdDataCache
) : ViewModel() {

    private var lastFetchedMovieResult: Flow<PagingData<PopularMovieWithDetailsModel>>? = null

    private val _isLoggedOut = MutableLiveData<Boolean>().apply { value = false }
    val isLoggedOut: LiveData<Boolean> get() = _isLoggedOut

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

    fun logout() {
        viewModelScope.launch {
            val b = sessionIdDataCache.loadSessionId()
            println("mLog: session key = $b")

            val logoutRequestBodyModel = LogoutRequestBodyModel(b)
            val a = logoutUseCase.execute(logoutRequestBodyModel)
            _isLoggedOut.value = a

        }
    }

}