package com.example.domain.repositories

import com.example.domain.models.LoginBodyModel
import com.example.domain.models.LogoutRequestBodyModel
import com.example.domain.models.SessionIdRequestBodyModel
import com.example.domain.utils.ResponseResult

interface AuthMovieRepository {

    suspend fun saveRequestToken()
    suspend fun login(loginBodyModel: LoginBodyModel): ResponseResult<Boolean>
    suspend fun saveSessionId(sessionIdModel: SessionIdRequestBodyModel): Boolean
    suspend fun logout(logoutRequestBodyModel: LogoutRequestBodyModel): Boolean
}