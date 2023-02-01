package com.sacramento.domain.repositories

import com.sacramento.domain.models.MovieCategoryModel
import com.sacramento.domain.models.GenreModel
import com.sacramento.domain.utils.ResponseResult

interface MovieCategoriesRepository {
    suspend fun getCategoriesList(): ResponseResult<List<MovieCategoryModel>>
    suspend fun getGenres(): List<GenreModel>
}