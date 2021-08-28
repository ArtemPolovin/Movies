package com.example.movies.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.utils.RequestTokenDataCache
import com.example.domain.models.LoginBodyModel
import com.example.domain.models.SessionIdRequestBodyModel
import com.example.domain.usecases.auth.LoginUseCase
import com.example.domain.usecases.auth.SaveRequestTokenUseCase
import com.example.domain.usecases.auth.SaveSessionIdUseCase
import com.example.domain.utils.ResponseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val saveRequestTokenUseCase: SaveRequestTokenUseCase,
    private val requestTokenDataCache: RequestTokenDataCache,
    private val loginUseCase: LoginUseCase,
    private val saveSessionIdUseCase: SaveSessionIdUseCase
) : ViewModel() {

    init {
        callSavingRequestToken()
    }

    private val _isLoginSuccess = MutableLiveData<ResponseResult<Boolean>>()
    val isLoginSuccess: LiveData<ResponseResult<Boolean>> get() = _isLoginSuccess

    private val _isSessionIdSaved = MutableLiveData<Boolean>().apply { value = false }
    val isSessionIdSaved: LiveData<Boolean>get() = _isSessionIdSaved

    private fun callSavingRequestToken() {
        viewModelScope.launch {
            saveRequestTokenUseCase()
        }
    }

    private fun login(loginBodyModel: LoginBodyModel) {
        viewModelScope.launch {
            _isLoginSuccess.value = loginUseCase.execute(loginBodyModel)
        }
    }

    fun receiveLoginAndPassword(userName: String, password: String) {
        val requestToken = requestTokenDataCache.loadRequestToken()
        val loginBodyModel = LoginBodyModel(password, requestToken, userName)

        login(loginBodyModel)
    }

    fun saveSessionId() {
        viewModelScope.launch {
            val model = SessionIdRequestBodyModel(requestTokenDataCache.loadRequestToken())
           _isSessionIdSaved.value =  saveSessionIdUseCase.save(model)
        }
    }

}