package com.sacramento.data.apimodels.auth

data class LoginErrorApiModel(
    val status_code: Int,
    val status_message: String,
    val success: Boolean
)