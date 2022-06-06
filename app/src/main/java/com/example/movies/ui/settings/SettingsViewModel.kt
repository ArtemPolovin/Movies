package com.example.movies.ui.settings

import android.app.Application
import android.content.Context
import android.os.Build
import android.webkit.CookieManager
import androidx.lifecycle.*
import com.example.data.cache.RequestTokenDataCache
import com.example.data.cache.SessionIdDataCache
import com.example.data.cache.SharedPrefLoginAndPassword
import com.example.data.cache.SharedPreferencesLoginRememberMe
import com.example.domain.models.LogoutRequestBodyModel
import com.example.domain.usecases.auth.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    application: Application,
    private val logoutUseCase: LogoutUseCase,
    private val sessionIdDataCache: SessionIdDataCache,
    private val requestTokenDataCache: RequestTokenDataCache,
    ): AndroidViewModel(application) {

    private val _isLoggedOut = MutableLiveData<Boolean>().apply { value = false }
    val isLoggedOut: LiveData<Boolean> get() = _isLoggedOut

    fun logout() {
        viewModelScope.launch {
            val logoutRequestBodyModel = LogoutRequestBodyModel(sessionIdDataCache.loadSessionId())

            val isLoggedOutFromApi = logoutUseCase.execute(logoutRequestBodyModel)
            if (isLoggedOutFromApi){
                deleteTokenFromCache()
                clearCookies()
            }
            _isLoggedOut.value = isLoggedOutFromApi
        }
    }

    private fun clearCookies() {
        CookieManager.getInstance().apply {
            removeAllCookies(null)
            flush()
        }
    }

    private fun deleteTokenFromCache() {
        sessionIdDataCache.removeSessionId()
        requestTokenDataCache.removeRequestToken()
    }
}