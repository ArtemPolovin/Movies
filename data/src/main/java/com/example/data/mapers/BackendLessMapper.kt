package com.example.data.mapers

import com.example.data.apimodels.movie_categories.MovieCategoriesApiModel
import com.example.domain.models.MovieCategoriesCellModel

class BackendLessMapper {

    fun mapMovieCategoryApiToModelList(movieCategoryApi: MovieCategoriesApiModel): List<MovieCategoriesCellModel> {
        return movieCategoryApi.map {
            MovieCategoriesCellModel(image = it.image, category = it.text)
        }
    }
}