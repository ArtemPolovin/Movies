package com.example.data.repositories_impl

import com.example.data.R
import com.example.data.mapers.MovieCategoriesMapper
import com.example.data.network.MoviesApi
import com.example.data.utils.MovieCategories.*
import com.example.domain.models.CategoryModel
import com.example.domain.models.GenreModel
import com.example.domain.repositories.MovieCategoriesRepository
import com.example.domain.utils.ResponseResult

import java.lang.Exception


class MovieCategoriesRepositoryImpl(
   private val movieCategoriesMapper: MovieCategoriesMapper
): MovieCategoriesRepository {

    override  fun getCategoriesList() : List<CategoryModel> =
        movieCategoriesMapper.getCategoriesListWithId(createCategoriesList())

    override fun getGenres(): List<GenreModel> =
         movieCategoriesMapper.mapJsonGenresToModelList()

    private fun createCategoriesList():List<CategoryModel> {

        return mutableListOf(
            CategoryModel(categoryName = POPULAR.categoryName, image = R.drawable.image_popular),
            CategoryModel(categoryName = TOP_RATED.categoryName, image = R.drawable.image_top_rated),
            CategoryModel(categoryName = UPCOMING.categoryName, image = R.drawable.image_upcoming),
            CategoryModel(categoryName = THRILLER.categoryName, image = R.drawable.image_thriller),
            CategoryModel(categoryName = COMEDY.categoryName, image = R.drawable.image_comedy),
            CategoryModel(categoryName = ACTION.categoryName, image = R.drawable.image_action),
            CategoryModel(categoryName = HORROR.categoryName, image = R.drawable.image_horror),
            CategoryModel(categoryName = FANTASY.categoryName, image = R.drawable.image_fantasy),
            CategoryModel(categoryName = DRAMA.categoryName, image = R.drawable.image_drama),
            CategoryModel(categoryName = CRIME.categoryName, image = R.drawable.image_crime),
            CategoryModel(categoryName = FAMILY.categoryName, image = R.drawable.image_family),
            CategoryModel(categoryName = WAR.categoryName, image = R.drawable.image_war),
            CategoryModel(categoryName = WESTERN.categoryName, image = R.drawable.image_western),
        )
    }


}