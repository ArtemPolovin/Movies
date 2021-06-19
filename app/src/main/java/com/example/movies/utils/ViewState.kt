package com.example.movies.utils


sealed class ViewState<out T : Any> {
        object Loading: ViewState<Nothing>()
        data class Success<out T: Any>(val data: T): ViewState<T>()
        data class Failure(val error: Throwable? = null, val message: String = ""): ViewState<Nothing>()
    }
