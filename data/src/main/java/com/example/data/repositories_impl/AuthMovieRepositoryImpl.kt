package com.example.data.repositories_impl

import com.example.data.mapers.ErrorLoginMapper
import com.example.data.network.AuthMovieAPIService
import com.example.data.utils.RequestTokenDataCache
import com.example.data.utils.SessionIdDataCache
import com.example.domain.models.LoginBodyModel
import com.example.domain.models.LogoutRequestBodyModel
import com.example.domain.models.SessionIdRequestBodyModel
import com.example.domain.repositories.AuthMovieRepository
import com.example.domain.utils.ResponseResult

class AuthMovieRepositoryImpl(
    private val authMovieApiService: AuthMovieAPIService,
    private val requestTokenDataCache: RequestTokenDataCache,
    private val errorLoginMapper: ErrorLoginMapper,
    private val sessionIdDataCache: SessionIdDataCache
) : AuthMovieRepository {

    override suspend fun saveRequestToken(): String {
        var requestToken = ""
        try {
            val response = authMovieApiService.getRequestToken()
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    requestTokenDataCache.saveRequestToken(body.request_token)
                    requestToken =  body.request_token
                }
            } else {
                Throwable(response.errorBody().toString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return requestToken
    }

    override suspend fun login(loginBodyModel: LoginBodyModel): ResponseResult<Boolean> {
        return try {
            val response = authMovieApiService.login(loginBodyModel)
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    return@let ResponseResult.Success(body.success)
                } ?: ResponseResult.Failure(message = "An unknown error occured")
            } else {
                response.errorBody()?.let { errorBody ->
                    ResponseResult.Failure(
                        message = errorLoginMapper.mapApiErrorMessageToString(
                            errorBody.string()
                        )
                    )
                } ?: ResponseResult.Failure(message = "Error, please try again")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseResult.Failure(e, "Couldn't reach the server. Check you internet connection")
        }
    }

    override suspend fun saveSessionId(sessionIdModel: SessionIdRequestBodyModel): Boolean {

        return try {
            val response = authMovieApiService.getSessionId(sessionIdModel)
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    sessionIdDataCache.saveSessionId(body.session_id)
                    return@let body.success
                } ?: false
            } else {
                Throwable("unsuccessful attempt to get the session id")
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun logout(logoutRequestBodyModel: LogoutRequestBodyModel): Boolean {
        return try {
            val response = authMovieApiService.logout(logoutRequestBodyModel)
            if (response.isSuccessful) {
                response.body()?.success ?: false
            } else false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

}


