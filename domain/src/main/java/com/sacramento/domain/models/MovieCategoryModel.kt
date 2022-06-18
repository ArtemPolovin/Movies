package com.sacramento.domain.models

data class MovieCategoryModel(
    val image: Int,
    var categoryName: String = "",
    val genreId: String
)