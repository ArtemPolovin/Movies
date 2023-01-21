package com.sacramento.data.repositories_impl

import com.sacramento.data.mapers.GuestSessionMapper
import com.sacramento.data.network.GuestSessionApiService
import com.sacramento.domain.models.GuestSessionModel
import com.sacramento.domain.repositories.GuestSessionRepository
import com.sacramento.domain.utils.ResponseResult
import java.io.IOException

class GuestSessionRepositoryImpl(
    private val guestSessionApi: GuestSessionApiService,
    private val guestSessionMapper: GuestSessionMapper
) : GuestSessionRepository {

    override suspend fun getGuestSession(): ResponseResult<GuestSessionModel> {
        return try {
            val response = guestSessionApi.createGuestSession()
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let ResponseResult.Success(
                        guestSessionMapper.mapGuestSessionApiToModel(it)
                    )
                } ?: ResponseResult.Failure(message = "Guest session request was not success")
            } else {
                ResponseResult.Failure(message = "Guest session request was not success")
            }
        } catch (e: IOException) {
            e.printStackTrace()
            ResponseResult.Failure(message = "Guest session request was not success")
        }
    }
}