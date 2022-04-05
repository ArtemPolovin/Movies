package com.example.movies.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val logoutUseCase: LogoutUseCase,
    private val sessionIdDataCache: SessionIdDataCache,
    private val loginSharedPreferencesRememberMe: SharedPreferencesLoginRememberMe,
    private val sharedPrefLoginAndPassword: SharedPrefLoginAndPassword,

    ): ViewModel() {

    private val _isLoggedOut = MutableLiveData<Boolean>().apply { value = false }
    val isLoggedOut: LiveData<Boolean> get() = _isLoggedOut

    fun logout() {
        viewModelScope.launch {
            val logoutRequestBodyModel = LogoutRequestBodyModel(sessionIdDataCache.loadSessionId())

            val isLoggedOutFromApi = logoutUseCase.execute(logoutRequestBodyModel)
            if (isLoggedOutFromApi) clearLoginRememberMeData()
            _isLoggedOut.value = isLoggedOutFromApi
        }
    }

    private fun clearLoginRememberMeData() {
        loginSharedPreferencesRememberMe.saveIsRememberMeChecked(false)
        sharedPrefLoginAndPassword.clearUserName()
        sharedPrefLoginAndPassword.clearPassword()
    }
}