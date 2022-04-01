package com.example.domain.models

data class MovieCategoryModel(
    val image: Int,
    var categoryName: String = "",
    val genreId: String
)