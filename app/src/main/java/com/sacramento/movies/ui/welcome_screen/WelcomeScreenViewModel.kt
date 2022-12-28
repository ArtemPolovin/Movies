package com.sacramento.movies.ui.welcome_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sacramento.domain.usecases.auth.LoadSessionIdUseCase
import com.sacramento.domain.usecases.movie_usecase.GetWatchListUseCase
import com.sacramento.domain.usecases.movie_usecase.InsertAllSavedMoviesFromAccountToDBUseCase
import com.sacramento.domain.utils.ResponseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeScreenViewModel @Inject constructor(
    private val loadSessionIdUseCase: LoadSessionIdUseCase,
) : ViewModel() {

    private val sessionId = loadSessionIdUseCase.execute()

    fun isAuthorized(): Boolean {
        return sessionId.isNotEmpty()
    }
}