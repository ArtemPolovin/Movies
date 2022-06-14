package com.example.movies.ui.settings

import android.app.Application
import androidx.lifecycle.*
import com.example.domain.models.LogoutRequestBodyModel
import com.example.domain.usecases.auth.LoadSessionIdUseCase
import com.example.domain.usecases.auth.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    application: Application,
    private val logoutUseCase: LogoutUseCase,
    private val loadSessionIdUseCase: LoadSessionIdUseCase,
    ): AndroidViewModel(application) {

    private val _isLoggedOut = MutableLiveData<Boolean>().apply { value = false }
    val isLoggedOut: LiveData<Boolean> get() = _isLoggedOut

    fun logout() {
        viewModelScope.launch {
            val logoutRequestBodyModel = LogoutRequestBodyModel(loadSessionIdUseCase.execute())
            _isLoggedOut.value = logoutUseCase.execute(logoutRequestBodyModel)
        }
    }

}