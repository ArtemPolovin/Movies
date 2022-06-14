package com.example.data.repositories_impl

import com.example.data.mapers.ErrorLoginMapper
import com.example.data.network.AuthMovieAPIService
import com.example.domain.models.LoginBodyModel
import com.example.domain.models.LogoutRequestBodyModel
import com.example.domain.models.SessionIdRequestBodyModel
import com.example.domain.repositories.AuthMovieRepository
import com.example.domain.repositories.CacheRepository
import com.example.domain.utils.ResponseResult

class AuthMovieRepositoryImpl(
    private val authMovieApiService: AuthMovieAPIService,
    private val errorLoginMapper: ErrorLoginMapper,
    private val cacheRepository: CacheRepository
) : AuthMovieRepository {

    // This function fetches request token from server and saves it to local cache
    override suspend fun saveRequestToken(): String {
        var requestToken = ""
        try {
            val response = authMovieApiService.getRequestToken()
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    cacheRepository.saveRequestToken(body.request_token)
                    requestToken = body.request_token
                }
            } else {
                Throwable(response.errorBody().toString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return requestToken
    }

    // This function takes user's username and password and request token and sends this data to server.
    // If login is success, the function return true
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

    // This function takes request token and sends it to server and receives session id and saves it to local cache
    override suspend fun saveSessionId(sessionIdModel: SessionIdRequestBodyModel): Boolean {

        return try {
            val response = authMovieApiService.getSessionId(sessionIdModel)
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    cacheRepository.saveSessionId(body.session_id)
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

    // This function takes session id and sends it with logout request to the server.
    // If the logout is success, the function returns true
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


