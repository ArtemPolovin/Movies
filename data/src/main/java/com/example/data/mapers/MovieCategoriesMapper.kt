package com.example.data.mapers

import android.content.Context
import com.example.data.R
import com.example.data.apimodels.genres.GenresApiModel
import com.example.domain.models.CategoryModel
import com.example.domain.models.GenreModel
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader

class MovieCategoriesMapper(private val gson: Gson, private val context:Context) {

    fun getCategoriesListWithId(
        categories: List<CategoryModel>,
    ): List<CategoryModel> {
         categories.forEach { categoryModel ->
            mapJsonGenresToModelList().forEach { genre ->

                if (categoryModel.categoryName == genre.name) {
                    categoryModel.genreId = genre.id.toString()
                }
            }
        }

        return categories
    }

    fun mapJsonGenresToModelList(): List<GenreModel> {
        return gson.fromJson(fromJsonFileToString(), Array<GenreModel>::class.java).toList()
    }

    private fun fromJsonFileToString(): String { //The method parses json file with list of genres data to string
        val inputStream = context.resources.openRawResource(R.raw.genres)
        val isReader = InputStreamReader(inputStream)
        val reader = BufferedReader(isReader)
        val strBuilder = StringBuilder()
        reader.use {
            var str = it.readLine()
            while (str != null) {
                strBuilder.append(str)
                str = it.readLine()
            }
        }
        inputStream.close()
        isReader.close()
        return strBuilder.toString()
    }
}