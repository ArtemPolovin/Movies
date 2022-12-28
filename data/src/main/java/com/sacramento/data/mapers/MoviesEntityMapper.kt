package com.sacramento.data.mapers

import com.sacramento.data.apimodels.movie_details.MovieDetailsModelApi
import com.sacramento.data.cache.SettingsDataCache
import com.sacramento.data.db.models.MovieWithGenresDBModel
import com.sacramento.data.db.tables.SavedMovieEntity
import com.sacramento.data.db.tables.movie_tables.GenreEntity
import com.sacramento.data.db.tables.movie_tables.MovieEntity
import com.sacramento.data.db.tables.movie_tables.MovieGenreCrossRef
import com.sacramento.data.utils.DEFAULT_ENGLISH_LANGUAGE_VALUE
import com.sacramento.data.utils.POSTER_BASE_URL
import com.sacramento.domain.models.MovieModel
import com.sacramento.domain.models.MovieWithDetailsModel
import com.sacramento.domain.models.SavedMovie

class MoviesEntityMapper(
    private val settingsDataCache: SettingsDataCache
) {

    //This function maps MovieWithDetailsModelApi to Entity model
    fun mapMovieModelToEntity(movie: MovieDetailsModelApi): MovieEntity {
        val curLanguage = settingsDataCache.getLanguage() ?: DEFAULT_ENGLISH_LANGUAGE_VALUE
        return MovieEntity(
            movieId = movie.id,
            releaseData = movie.release_date,
            popularityScore = movie.popularity,
            movieName = movie.title,
            rating = String.format("%.1f", movie.vote_average).toFloat(),
            poster = movie.poster_path,
            overview = movie.overview,
            backdropPoster = movie.backdrop_path,
            homePageUrl = movie.homepage,
            vote_count = movie.vote_count.toString(),
            language = curLanguage
        )
    }

    //This function takes all saved movies from DB and maps the list to model list
    fun mapMovieEntityListToMovieWithDetailsModelList(movieEntityList: Map<MovieEntity, List<GenreEntity>>): List<MovieWithDetailsModel> {

        val moviesList = mutableListOf<MovieWithDetailsModel>()

        for ((key, value) in movieEntityList) {
            moviesList.add(
                MovieWithDetailsModel(
                    id = key.movieId,
                    releaseData = key.releaseData,
                    popularityScore = key.popularityScore.toString(),
                    movieName = key.movieName,
                    rating = key.rating,
                    poster = "$POSTER_BASE_URL${key.poster}",
                    overview = key.overview,
                    backdropImage = "${POSTER_BASE_URL}${key.backdropPoster}",
                    homePageUrl = key.homePageUrl,
                    voteCount = key.vote_count,
                    genres = value.joinToString(", ") { it.genreName }
                )
            )
        }
        return moviesList
    }

    fun mapMovieApiToMovieGenreCrossRefEntity(movieApi: MovieDetailsModelApi): List<MovieGenreCrossRef>? {
        val movieId = movieApi.id
        return movieApi.genres?.map {
            MovieGenreCrossRef(movieId, it.id.toString())
        }
    }

     fun mapMovieEntityListToMovieModelList(movieEntityList: Map<MovieEntity, List<GenreEntity>>): List<MovieModel> {
         return movieEntityList.keys.map { movieEntity ->
             MovieModel(
                 movieId = movieEntity.movieId,
                 title = movieEntity.movieName,
                 rating = movieEntity.rating?.toDouble(),
                 poster = "$POSTER_BASE_URL${movieEntity.poster}",
                 voteCount = movieEntity.vote_count?.toInt(),
             )
         }
     }

    fun mapMovieEntityListToMovieModelList(movieEntityList: List<MovieWithGenresDBModel>): List<MovieModel> {
        return movieEntityList.map { movieEntity ->
            MovieModel(
                movieId = movieEntity.movie.movieId,
                title = movieEntity.movie.movieName,
                rating = movieEntity.movie.rating?.toDouble(),
                poster = "$POSTER_BASE_URL${movieEntity.movie.poster}",
                voteCount = movieEntity.movie.vote_count?.toInt(),
            )
        }
    }

    fun mapMovieModelListToSavedMoviesEntityList(movieList: List<MovieModel>): List<SavedMovieEntity> {

    return  movieList.map {
            SavedMovieEntity(
                movieId = it.movieId,
                movieName = it.title,
                rating = it.rating,
                poster = it.poster,
                vote_count = it.voteCount
            )
        }
    }

    fun mapSavedMoviesEntityToMovieModelList(savedMoviesEntity: List<SavedMovie>): List<MovieModel> {
        return  savedMoviesEntity.map { movieEntity ->
            movieEntity as SavedMovieEntity
            MovieModel(
                movieId = movieEntity.movieId,
                title = movieEntity.movieName,
                rating = movieEntity.rating,
                poster = movieEntity.poster,
                voteCount = movieEntity.vote_count
            )
        }
    }

    fun mapMovieModelToSavedMovieEntity(movie: MovieModel): SavedMovieEntity {
        return SavedMovieEntity(
            movieId = movie.movieId,
            movieName = movie.title,
            rating = movie.rating,
            poster = movie.poster,
            vote_count = movie.voteCount
        )
    }

    fun mapMovieEntityToMovieModel(movieEntity: Map<MovieEntity, List<GenreEntity>>): MovieWithDetailsModel {
        val list = mutableListOf<MovieWithDetailsModel>()

        for ((key, value) in movieEntity) {
            list.add(
                MovieWithDetailsModel(
                    releaseData = key.releaseData,
                    popularityScore = key.popularityScore.toString(),
                    movieName = key.movieName,
                    rating = key.rating,
                    poster = "$POSTER_BASE_URL${key.poster}",
                    overview = key.overview,
                    backdropImage = "$POSTER_BASE_URL${key.backdropPoster}",
                    genres = value.joinToString(", ") { it.genreName },
                    homePageUrl = key.homePageUrl,
                    id = key.movieId,
                    voteCount = key.vote_count
                )
            )
        }
        return list.first()
    }

}