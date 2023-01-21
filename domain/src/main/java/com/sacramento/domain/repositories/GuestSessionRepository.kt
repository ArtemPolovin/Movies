package com.sacramento.domain.repositories

import com.sacramento.domain.models.GuestSessionModel
import com.sacramento.domain.utils.ResponseResult

interface GuestSessionRepository {
    suspend fun getGuestSession(): ResponseResult<GuestSessionModel>
}