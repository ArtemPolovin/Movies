package com.sacramento.movies.ui.session

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sacramento.data.cache.GuestSessionSharedPref
import com.sacramento.domain.models.GuestSessionModel
import com.sacramento.domain.usecases.movie_usecase.GetGuestSessionUseCase
import com.sacramento.domain.utils.ResponseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionSelectionViewModel @Inject constructor(
    private val getGuestSessionUseCase: GetGuestSessionUseCase,
    private val guestSessionSharedPref: GuestSessionSharedPref
) : ViewModel() {

    private val _guestSession = MutableLiveData<ResponseResult<GuestSessionModel>>()
    val guestSession: LiveData<ResponseResult<GuestSessionModel>> get() = _guestSession

    fun fetchGuestSession() {
        _guestSession.value = ResponseResult.Loading
        viewModelScope.launch {
            _guestSession.value = getGuestSessionUseCase.invoke()
        }
    }

    fun saveGuestSessionId(guestSessionId: String) {
        guestSessionSharedPref.saveGuestSessionId(guestSessionId)
    }

}