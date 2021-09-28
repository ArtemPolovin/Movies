package com.example.data.mapers

import com.example.data.apimodels.genres.GenresApiModel
import com.example.data.apimodels.movie_categories.MovieCategoriesApiModel
import com.example.data.utils.MovieCategories
import com.example.domain.models.MovieCategoriesCellModel

class BackendLessMapper {

    fun mapMovieCategoryApiToModelList(
        movieCategoryApi: MovieCategoriesApiModel,
        genresApi: GenresApiModel
    ): List<MovieCategoriesCellModel> {

        val movieCategoriesList = mutableListOf<MovieCategoriesCellModel>()

        // add first 3 categories to the list
        movieCategoriesList.addAll(movieCategoryApi.filter {
            it.text == MovieCategories.Upcoming.category ||
                    it.text == MovieCategories.TopRated.category ||
                    it.text == MovieCategories.Popular.category
        }.map {movieCategories ->
            MovieCategoriesCellModel(
                image = movieCategories.image,
                category = movieCategories.text,
            )
        })

          // add genres to the list
        movieCategoryApi.forEach { movieCategories ->
            genresApi.genres.forEach { genre ->

                if (genre.name == movieCategories.text) {
                    movieCategoriesList.add(
                        MovieCategoriesCellModel(
                            image = movieCategories.image,
                            category = genre.name,
                            genreId = genre.id.toString()
                        )
                    )
                }
            }
        }
        return movieCategoriesList
    }
}