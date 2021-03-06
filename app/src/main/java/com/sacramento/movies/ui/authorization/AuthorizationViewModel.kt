package com.sacramento.movies.ui.authorization

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sacramento.domain.models.SessionIdRequestBodyModel
import com.sacramento.domain.usecases.auth.LoadRequestTokenUseCase
import com.sacramento.domain.usecases.auth.LogoutFromWebPageUseCase
import com.sacramento.domain.usecases.auth.SaveRequestTokenUseCase
import com.sacramento.domain.usecases.auth.SaveSessionIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    application: Application,
    private val saveRequestTokenUseCase: SaveRequestTokenUseCase,
    private val loadRequestTokenUseCase: LoadRequestTokenUseCase,
    private val saveSessionIdUseCase: SaveSessionIdUseCase,
    private val logoutFromWebPageUseCase: LogoutFromWebPageUseCase
) : AndroidViewModel(application) {

    private val _requestToken = MutableLiveData<String>()
    val requestToken: LiveData<String> get() = _requestToken

    private val _isSessionIdSaved = MutableLiveData<Boolean>().apply { value = false }
    val isSessionIdSaved: LiveData<Boolean> get() = _isSessionIdSaved

    init {
        getRequestToken()
    }

     fun getRequestToken() {
        viewModelScope.launch {
            _requestToken.value = saveRequestTokenUseCase.execute()
        }
    }

    fun isAuthorizationApproved(url:String): Boolean {
       return (url.contains("/allow"))
    }

     fun saveSessionId() {
        viewModelScope.launch {
            val model = SessionIdRequestBodyModel(loadRequestTokenUseCase.execute())
           _isSessionIdSaved.value =  saveSessionIdUseCase.save(model)
        }
    }


    fun logout() {
        logoutFromWebPageUseCase.execute()
    }

   /*  fun clearCookies() {
        CookieManager.getInstance().apply {
            removeAllCookies(null)
            flush()
        }
    }

     fun deleteTokenFromCache() {
        sessionIdDataCache.removeSessionId()
        requestTokenDataCache.removeRequestToken()
    }*/


}