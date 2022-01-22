package com.example.domain.repositories

import com.example.domain.models.MovieCategoryModel
import com.example.domain.models.GenreModel
import com.example.domain.utils.ResponseResult

interface MovieCategoriesRepository {
    suspend fun getCategoriesList(): ResponseResult<List<MovieCategoryModel>>
    suspend fun getGenres(): List<GenreModel>
}