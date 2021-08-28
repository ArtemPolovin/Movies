package com.example.data.apimodels.auth

data class LoginResponseApiModel(
    val expires_at: String,
    val request_token: String,
    val success: Boolean
)