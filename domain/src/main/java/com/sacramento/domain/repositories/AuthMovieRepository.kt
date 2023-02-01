package com.sacramento.domain.repositories

import com.sacramento.domain.models.LoginBodyModel
import com.sacramento.domain.models.LogoutRequestBodyModel
import com.sacramento.domain.models.SessionIdRequestBodyModel
import com.sacramento.domain.utils.ResponseResult

interface AuthMovieRepository {

    suspend fun saveRequestToken(): String
    suspend fun login(loginBodyModel: LoginBodyModel): ResponseResult<Boolean>
    suspend fun saveSessionId(sessionIdModel: SessionIdRequestBodyModel): Boolean
    suspend fun logout(logoutRequestBodyModel: LogoutRequestBodyModel): Boolean
}