package com.example.data.repositories_impl

import com.example.data.R
import com.example.data.cache.SettingsDataCache
import com.example.data.mapers.MovieGenresMapper
import com.example.data.network.MoviesApi
import com.example.data.utils.MovieCategories.*
import com.example.domain.models.MovieCategoryModel
import com.example.domain.models.GenreModel
import com.example.domain.repositories.MovieCategoriesRepository
import com.example.domain.utils.ResponseResult
import java.io.IOException


class MovieCategoriesRepositoryImpl(
    private val movieGenresMapper: MovieGenresMapper,
    private val settingsDataCache: SettingsDataCache,
    private val moviesApi: MoviesApi
): MovieCategoriesRepository {

    // This function takes all genres list and maps it to MovieCategoryModels list and returns it
    override suspend fun getCategoriesList() : ResponseResult<List<MovieCategoryModel>> {
        val genresList = getGenres()
      return  if(genresList.isNotEmpty()){
            ResponseResult.Success( movieGenresMapper.mapGenresListApiToCategoriesList(createCategoriesList(), genresList))
        }else{
            ResponseResult.Failure(message = "The genres list is empty")
        }
    }

    // This function fetches all genres list from server and maps the genres list to GenreModels list and returns it
    override suspend fun getGenres(): List<GenreModel> {
        return try {
            val response = moviesApi.getGenresList(settingsDataCache.getLanguage())
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    return@let movieGenresMapper.mapGenresApiListToModelList(body)
                }?: movieGenresMapper.mapJsonGenresToModelList()
            } else movieGenresMapper.mapJsonGenresToModelList()
        } catch (e: IOException) {
            e.printStackTrace()
            movieGenresMapper.mapJsonGenresToModelList()
        }
    }


     // This function creates MovieCategoryModels list with genre id and image.
     // Only "Top Rated", "Popular" and "Upcoming" categories do not have genre id
    private fun createCategoriesList():List<MovieCategoryModel> {

        return mutableListOf(
            MovieCategoryModel(genreId =  "",categoryName = POPULAR.categoryName, image = R.drawable.image_popular),
            MovieCategoryModel(genreId =  "",categoryName = TOP_RATED.categoryName, image = R.drawable.image_top_rated),
            MovieCategoryModel(genreId =  "",categoryName = UPCOMING.categoryName, image = R.drawable.image_upcoming),
            MovieCategoryModel(genreId = THRILLER.genreId, image = R.drawable.image_thriller),
            MovieCategoryModel(genreId = COMEDY.genreId, image = R.drawable.image_comedy),
            MovieCategoryModel(genreId = ACTION.genreId, image = R.drawable.image_action),
            MovieCategoryModel(genreId = HORROR.genreId, image = R.drawable.image_horror),
            MovieCategoryModel(genreId = FANTASY.genreId, image = R.drawable.image_fantasy),
            MovieCategoryModel(genreId = DRAMA.genreId, image = R.drawable.image_drama),
            MovieCategoryModel(genreId = CRIME.genreId, image = R.drawable.image_crime),
            MovieCategoryModel(genreId = FAMILY.genreId, image = R.drawable.image_family),
            MovieCategoryModel(genreId = WAR.genreId, image = R.drawable.image_war),
            MovieCategoryModel(genreId = WESTERN.genreId, image = R.drawable.image_western),
        )
    }


}