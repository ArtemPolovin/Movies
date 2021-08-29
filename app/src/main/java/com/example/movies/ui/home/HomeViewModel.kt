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
import com.example.movies.utils.SharedPrefLoginAndPassword
import com.example.movies.utils.SharedPreferencesLoginRememberMe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val moviePagingSource: MoviePagingSource,
    private val logoutUseCase: LogoutUseCase,
    private val sessionIdDataCache: SessionIdDataCache,
    private val loginSharedPreferencesRememberMe: SharedPreferencesLoginRememberMe,
    private val sharedPrefLoginAndPassword: SharedPrefLoginAndPassword
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
            val logoutRequestBodyModel = LogoutRequestBodyModel(sessionIdDataCache.loadSessionId())

            val isLoggedOutFormApi = logoutUseCase.execute(logoutRequestBodyModel)
            if (isLoggedOutFormApi) clearLoginRememberMeData()
            _isLoggedOut.value = isLoggedOutFormApi
        }
    }

    private fun clearLoginRememberMeData() {
        loginSharedPreferencesRememberMe.saveIsRememberMeChecked(false)
        sharedPrefLoginAndPassword.clearUserName()
        sharedPrefLoginAndPassword.clearPassword()
    }

}