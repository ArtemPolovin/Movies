package com.example.movies.ui.welcome_screen

import androidx.lifecycle.ViewModel
import com.example.data.cache.SessionIdDataCache
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeScreenViewModel @Inject constructor(
    private val sessionIdDataCache: SessionIdDataCache
) : ViewModel() {

    fun isAuthorized(): Boolean {
        return sessionIdDataCache.loadSessionId().isNotEmpty()
    }
}