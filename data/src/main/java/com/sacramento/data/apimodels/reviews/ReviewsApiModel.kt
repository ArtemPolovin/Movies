package com.sacramento.data.apimodels.reviews

data class ReviewsApiModel(
    val id: Int,
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)