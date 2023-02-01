package com.sacramento.data.network

import com.sacramento.data.apimodels.auth.LoginResponseApiModel
import com.sacramento.data.apimodels.auth.LogoutApiModel
import com.sacramento.data.apimodels.auth.RequestTokenApiModel
import com.sacramento.data.apimodels.auth.SessionIdApiModel
import com.sacramento.data.utils.API_KEY
import com.sacramento.data.utils.MOVIES_API_BASE_URL
import com.sacramento.domain.models.LoginBodyModel
import com.sacramento.domain.models.LogoutRequestBodyModel
import com.sacramento.domain.models.SessionIdRequestBodyModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface AuthMovieAPIService {

    @GET("/3/authentication/token/new")
    suspend fun getRequestToken(): Response<RequestTokenApiModel>

    @POST("/3/authentication/token/validate_with_login")
    suspend fun login(@Body loginBodyModel: LoginBodyModel): Response<LoginResponseApiModel>

    @POST("/3/authentication/session/new")
    suspend fun getSessionId(@Body sessionIdModel: SessionIdRequestBodyModel): Response<SessionIdApiModel>

    @HTTP(method = "DELETE", path = "/3/authentication/session", hasBody = true)
    suspend fun logout(@Body logoutRequestBody: LogoutRequestBodyModel): Response<LogoutApiModel>

    companion object {
        operator fun invoke(): AuthMovieAPIService {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("api_key", API_KEY)
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()
                return@Interceptor chain.proceed(request)

            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor).build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(MOVIES_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AuthMovieAPIService::class.java)
        }
    }

}