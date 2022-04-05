package com.example.movies.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.cache.RequestTokenDataCache
import com.example.data.cache.SharedPrefLoginAndPassword
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
    private val saveSessionIdUseCase: SaveSessionIdUseCase,
//    private val sharedPrefLoginRememberMe: SharedPreferencesLoginRememberMe,
    private val sharedPrefLoginAndPassword: SharedPrefLoginAndPassword,
) : ViewModel() {

    init {
        // skipLoginIfRememberMeChecked()
        getRequestToken()
    }


//    private val _isLoginSuccess = MutableLiveData<ResponseResult<Boolean>>()
//    val isLoginSuccess: LiveData<ResponseResult<Boolean>> get() = _isLoginSuccess

    private val _isSessionIdSaved = MutableLiveData<Boolean>().apply { value = false }
    val isSessionIdSaved: LiveData<Boolean> get() = _isSessionIdSaved

//    private val _isRememberMeChecked = MutableLiveData<Boolean>().apply { value = false }
//    val isRememberMeChecked: LiveData<Boolean> get() = _isRememberMeChecked

//    private val _loginError = MutableLiveData<Boolean>()
//    val loginError: LiveData<Boolean>get() = _loginError

    private val _loginErrorText = MutableLiveData<String>()
    val loginErrorText: LiveData<String> get() = _loginErrorText

    private fun getRequestToken() {
        viewModelScope.launch { saveRequestTokenUseCase() }
    }

//    private fun skipLoginIfRememberMeChecked() {
//        viewModelScope.launch {
//            val requestToken = viewModelScope.async { getRequestToken() }
//            val isChecked = sharedPrefLoginRememberMe.loadIsRememberMeChecked()
//            if(isChecked) skipLoginScreen(requestToken.await())
//        }
//    }

    fun receiveLoginAndPassword(userName: String, password: String) {
        val requestToken = requestTokenDataCache.loadRequestToken()
        val loginBodyModel = LoginBodyModel(password, requestToken, userName)

        login(loginBodyModel)
    }

    private fun login(loginBodyModel: LoginBodyModel) {
        viewModelScope.launch {
            // _isLoginSuccess.value = loginUseCase.execute(loginBodyModel)
            val requestLogin = loginUseCase.execute(loginBodyModel)

            when (requestLogin) {
                is ResponseResult.Failure -> {
                    _loginErrorText.value = requestLogin.message
                }
                is ResponseResult.Success -> {
                    saveSessionId()
                }
                else -> _loginErrorText.value = "Login is not success. Please try again"
            }
        }
    }


    private fun saveSessionId() {
        viewModelScope.launch {
            val model = SessionIdRequestBodyModel(requestTokenDataCache.loadRequestToken())
            _isSessionIdSaved.value = saveSessionIdUseCase.save(model)
        }
    }

    fun saveUserNameAndPassword(userName: String, password: String) {
        sharedPrefLoginAndPassword.saveUserName(userName)
        sharedPrefLoginAndPassword.savePassword(password)
    }

    /* fun showLoginErrorText(errorText: String) {
         _loginErrorText.value = errorText
     }*/

//    private fun skipLoginScreen(requestToken: String) {
//            viewModelScope.launch {
//                val userName = sharedPrefLoginAndPassword.loadUserName()
//                val password = sharedPrefLoginAndPassword.loadPassword()
//                val loginBodyModel = LoginBodyModel(password, requestToken, userName)
//                val sessionIdRequestBodyModel = SessionIdRequestBodyModel(requestToken)
//
//                when (loginUseCase.execute(loginBodyModel)) {
//                    is ResponseResult.Success -> {
//                        if (saveSessionIdUseCase.save(sessionIdRequestBodyModel)) {
//                            _isRememberMeChecked.value = true
//                        }
//                    }
//                }
//            }
//    }

    /*private fun checkIfLoginIsSuccess() {
        viewModel.isLoginSuccess.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseResult.Failure -> {
                    binding.textError.visibility = View.VISIBLE
                    binding.textError.text = it.message
                }
                is ResponseResult.Success -> {
                    saveSessionId()
                }
            }
        }
    }
*/

}