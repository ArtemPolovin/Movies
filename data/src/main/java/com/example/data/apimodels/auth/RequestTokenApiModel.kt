package com.example.data.apimodels.auth

import androidx.annotation.Keep

data class RequestTokenApiModel(
    val expires_at: String,
    val request_token: String,
    val success: Boolean,
    val status_message: String,
    val status_code: Int
)