package com.sacramento.data.apimodels.auth

data class RequestTokenApiModel(
    val expires_at: String,
    val request_token: String,
    val success: Boolean,
    val status_message: String,
    val status_code: Int
)