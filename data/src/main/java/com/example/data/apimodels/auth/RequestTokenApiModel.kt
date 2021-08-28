package com.example.data.apimodels.auth

data class RequestTokenApiModel(
    val expires_at: String,
    val request_token: String,
    val success: Boolean
)