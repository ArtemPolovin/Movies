package com.sacramento.data.mapers

import android.content.Context
import com.sacramento.data.R
import com.sacramento.data.apimodels.genres.GenresApiModel
import com.sacramento.domain.models.MovieCategoryModel
import com.sacramento.domain.models.GenreModel
import com.google.gson.Gson

class MovieGenresMapper(private val gson: Gson, private val context: Context) {

    //This function adds genre names to existing movieCategoryModels by id
    fun mapGenresListApiToCategoriesList(
        movieCategories: List<MovieCategoryModel>,
        genresList: List<GenreModel>
    ): List<MovieCategoryModel> {

        movieCategories.forEach { categoryModel ->
            genresList.forEach { genre ->
                if (categoryModel.genreId == genre.id) {
                    categoryModel.categoryName = genre.name
                }
            }
        }

        return movieCategories
    }

    // This function maps GenresApiMode list  to model list
    fun mapGenresApiListToModelList(genreApiModel: GenresApiModel): List<GenreModel> {
        return genreApiModel.genres.map { genre ->
            GenreModel(id = genre.id.toString(),name = genre.name)
        }
    }

    // This function takes json file with genre models from raw folder and parses it to model list
    fun mapJsonGenresToModelList(): List<GenreModel> {
        val json = context.resources.openRawResource(R.raw.genres).bufferedReader()
            .use { it.readText() }
        return gson.fromJson(json, Array<GenreModel>::class.java).toList()
    }
}