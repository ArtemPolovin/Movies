package com.sacramento.data.network

import com.sacramento.data.BuildConfig
import com.sacramento.data.apimodels.guest_session.GuestSessionApiModel
import com.sacramento.data.utils.MOVIES_API_BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface GuestSessionApiService {

    @GET("/3/authentication/guest_session/new")
    suspend fun createGuestSession(): Response<GuestSessionApiModel>

    companion object {
        operator fun invoke(): GuestSessionApiService {
            val requestInterceptor = Interceptor{chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("api_key", BuildConfig.TMDB_APY_KEY)
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
                .create(GuestSessionApiService::class.java)
        }
    }

}