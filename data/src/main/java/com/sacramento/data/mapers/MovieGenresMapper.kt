package com.sacramento.data.mapers

import android.content.Context
import com.google.gson.Gson
import com.sacramento.data.R
import com.sacramento.data.apimodels.genres.GenresApiModel
import com.sacramento.data.cache.SettingsDataCache
import com.sacramento.data.db.tables.movie_tables.GenreEntity
import com.sacramento.data.utils.DEFAULT_ENGLISH_LANGUAGE_VALUE
import com.sacramento.domain.models.GenreModel
import com.sacramento.domain.models.MovieCategoryModel

class MovieGenresMapper(
    private val gson: Gson,
    private val context: Context,
    private val settingsDataCache: SettingsDataCache
) {

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
            GenreModel(id = genre.id.toString(), name = genre.name)
        }
    }

    // This function takes json file with genre models from raw folder and parses it to model list
    fun mapJsonGenresToModelList(): List<GenreModel> {
        val json = context.resources.openRawResource(R.raw.genres).bufferedReader()
            .use { it.readText() }
        return gson.fromJson(json, Array<GenreModel>::class.java).toList()
    }

    fun mapGenresModelToGenresEntity(genresApi: GenresApiModel): List<GenreEntity> {
        return genresApi.genres.map {
            GenreEntity(
                genreId = it.id.toString(),
                genreName = it.name,
                language = settingsDataCache.getLanguage()?: DEFAULT_ENGLISH_LANGUAGE_VALUE
            )
        }
    }
}