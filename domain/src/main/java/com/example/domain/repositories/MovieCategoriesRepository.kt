package com.example.domain.repositories

import com.example.domain.models.CategoryModel
import com.example.domain.models.GenreModel
import com.example.domain.utils.ResponseResult

interface MovieCategoriesRepository {
     fun getCategoriesList(): List<CategoryModel>
    fun getGenres(): List<GenreModel>
}