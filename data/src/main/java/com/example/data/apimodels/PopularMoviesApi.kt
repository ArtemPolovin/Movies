package com.example.data.apimodels

data class PopularMoviesApi(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)