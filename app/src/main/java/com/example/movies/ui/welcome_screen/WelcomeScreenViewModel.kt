package com.example.movies.ui.welcome_screen

import androidx.lifecycle.ViewModel
import com.example.domain.usecases.auth.LoadSessionIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeScreenViewModel @Inject constructor(
    private val loadSessionIdUseCase: LoadSessionIdUseCase
) : ViewModel() {

    fun isAuthorized(): Boolean {
        return loadSessionIdUseCase.execute().isNotEmpty()
    }
}