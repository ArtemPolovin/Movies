package com.sacramento.domain.models

data class GuestSessionModel(
    val expireAt: String,
    val guestSessionId: String,
    val session: Boolean
)