package com.sacramento.data.mapers

import com.sacramento.data.apimodels.movie_details.Genre
import com.sacramento.data.apimodels.movie_details.MovieDetailsModelApi
import com.sacramento.data.cache.SettingsDataCache
import com.sacramento.data.db.models.MovieWithGenresDBModel
import com.sacramento.data.db.tables.movie_tables.GenreEntity
import com.sacramento.data.db.tables.movie_tables.MovieGenreCrossRef
import com.sacramento.data.db.tables.movie_tables.SavedMovieEntity
import com.sacramento.data.utils.DEFAULT_ENGLISH_LANGUAGE_VALUE
import com.sacramento.data.utils.POSTER_BASE_URL
import com.sacramento.domain.models.GenreModel
import com.sacramento.domain.models.MovieModel
import com.sacramento.domain.models.MovieWithDetailsModel
import kotlin.math.roundToInt

class MoviesEntityMapper(
    private val settingsDataCache: SettingsDataCache
) {

    //This function maps MovieWithDetailsModelApi to Entity model
    fun mapMovieModelToEntity(movie: MovieDetailsModelApi): SavedMovieEntity {
        val curLanguage = settingsDataCache.getLanguage()?: DEFAULT_ENGLISH_LANGUAGE_VALUE
        return SavedMovieEntity(
            movieId = movie.id,
            releaseData = movie.release_date,
            popularityScore = movie.popularity,
            movieName = movie.title,
            rating =  String.format("%.1f",movie.vote_average).toFloat(),
            poster = movie.poster_path,
            overview = movie.overview,
            backdropPoster = movie.backdrop_path,
            homePageUrl = movie.homepage,
            vote_count = movie.vote_count.toString(),
            language = curLanguage
        )
    }

    //This function takes all saved movies from DB and maps the list to model list
    fun mapMovieEntityListToMovieWithDetailsModelList(movieEntityList: Map<SavedMovieEntity,List<GenreEntity>>):List<MovieWithDetailsModel> {

        val moviesList = mutableListOf<MovieWithDetailsModel>()

        for ((key, value) in movieEntityList) {
            moviesList.add(
                MovieWithDetailsModel(
                    id = key.movieId,
                    releaseData = key.releaseData,
                    popularityScore = key.popularityScore.toString(),
                    movieName = key.movieName,
                    rating = key.rating,
                    poster =  "$POSTER_BASE_URL${key.poster}",
                    overview = key.overview,
                    backdropImage =  "${POSTER_BASE_URL}${key.backdropPoster}",
                    homePageUrl = key.homePageUrl,
                    voteCount =key.vote_count,
                    genres = value.joinToString(", "){it.genreName}
                )
            )
        }
        return moviesList
    }

    fun mapMovieApiToMovieGenreCrossRefEntity(movieApi: MovieDetailsModelApi): List<MovieGenreCrossRef>? {
        val movieId = movieApi.id
        return movieApi.genres?.map {
            MovieGenreCrossRef(movieId,it.id.toString())
        }
    }

    fun mapMovieEntityListToMovieModelList(movieEntityList: List<MovieWithGenresDBModel>):List<MovieModel> {
        return movieEntityList.map { movieEntity ->
            MovieModel(
                movieId = movieEntity.movie.movieId,
                title = movieEntity.movie.movieName,
                rating = movieEntity.movie.rating?.toDouble(),
                poster =  "$POSTER_BASE_URL${movieEntity.movie.poster}",
                voteCount = movieEntity.movie.vote_count?.toInt(),
            )
        }
    }

}