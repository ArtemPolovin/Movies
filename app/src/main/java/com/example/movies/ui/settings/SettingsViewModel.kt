package com.example.movies.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.utils.SessionIdDataCache
import com.example.data.utils.SharedPrefLoginAndPassword
import com.example.data.utils.SharedPreferencesLoginRememberMe
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