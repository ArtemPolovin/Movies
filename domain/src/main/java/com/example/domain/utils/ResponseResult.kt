package com.example.domain.utils

sealed class ResponseResult<out T: Any> {
    object Loading: ResponseResult<Nothing>()
    data class Success<out T: Any>(val data: T): ResponseResult<T>()
    data class Failure(val error: Throwable? = null, val message: String = ""): ResponseResult<Nothing>()
}