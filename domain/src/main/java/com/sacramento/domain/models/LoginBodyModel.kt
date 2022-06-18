package com.sacramento.domain.models

data class LoginBodyModel(
    val password: String,
    val request_token: String,
    val username: String
)