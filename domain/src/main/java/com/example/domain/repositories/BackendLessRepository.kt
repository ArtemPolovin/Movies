package com.example.domain.repositories

import com.example.domain.models.MovieCategoriesCellModel
import com.example.domain.utils.ResponseResult

interface BackendLessRepository {
    suspend fun getMovieCategoriesImages():ResponseResult<List<MovieCategoriesCellModel>>
}