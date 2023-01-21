package com.sacramento.data.apimodels.guest_session

data class GuestSessionApiModel(
    val expires_at: String,
    val guest_session_id: String,
    val success: Boolean
)