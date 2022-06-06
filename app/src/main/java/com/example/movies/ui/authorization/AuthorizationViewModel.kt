package com.example.movies.ui.authorization

import android.app.Application
import android.webkit.CookieManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.data.cache.RequestTokenDataCache
import com.example.data.cache.SessionIdDataCache
import com.example.domain.models.SessionIdRequestBodyModel
import com.example.domain.usecases.auth.SaveRequestTokenUseCase
import com.example.domain.usecases.auth.SaveSessionIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    application: Application,
    private val saveRequestTokenUseCase: SaveRequestTokenUseCase,
    private val requestTokenDataCache: RequestTokenDataCache,
    private val saveSessionIdUseCase: SaveSessionIdUseCase,
    private val sessionIdDataCache: SessionIdDataCache
) : AndroidViewModel(application) {

    private val _requestToken = MutableLiveData<String>()
    val requestToken: LiveData<String> get() = _requestToken

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

    suspend fun saveSessionId(): Boolean {
            val model = SessionIdRequestBodyModel(requestTokenDataCache.loadRequestToken())
            return saveSessionIdUseCase.save(model)
    }

     fun clearCookies() {
        CookieManager.getInstance().apply {
            removeAllCookies(null)
            flush()
        }
    }

     fun deleteTokenFromCache() {
        sessionIdDataCache.removeSessionId()
        requestTokenDataCache.removeRequestToken()
    }


}