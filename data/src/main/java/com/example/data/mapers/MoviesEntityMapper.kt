package com.example.data.mapers

import com.example.data.db.tables.movie_tables.SavedMovieEntity
import com.example.domain.models.PopularMovieWithDetailsModel

class MoviesEntityMapper {
    fun mapMovieModelToEntity(movie: PopularMovieWithDetailsModel): SavedMovieEntity {
        return SavedMovieEntity(
            movieId = movie.id,
            releaseData = movie.releaseData,
            popularityScore = movie.popularityScore,
            movieName = movie.movieName,
            rating = movie.rating,
            poster = movie.poster,
            overview = movie.poster,
            backdropPoster = movie.backdropPoster,
            genres = movie.genres,
            homePageUrl = movie.homePageUrl,
        )
    }

    fun mapMovieEntityToMovieModel(movieEntity: SavedMovieEntity): PopularMovieWithDetailsModel {
        return PopularMovieWithDetailsModel(
            id = movieEntity.movieId,
            releaseData = movieEntity.releaseData,
            popularityScore = movieEntity.popularityScore,
            movieName = movieEntity.movieName,
            rating = movieEntity.rating,
            poster = movieEntity.poster,
            overview = movieEntity.poster,
            backdropPoster = movieEntity.backdropPoster,
            genres = movieEntity.genres,
            homePageUrl = movieEntity.homePageUrl
        )
    }

    fun mapMovieEntityListToModelList(movieEntityList: List<SavedMovieEntity>): List<PopularMovieWithDetailsModel> {
        return movieEntityList.map { movieEntity ->
            PopularMovieWithDetailsModel(
                id = movieEntity.movieId,
                releaseData = movieEntity.releaseData,
                popularityScore = movieEntity.popularityScore,
                movieName = movieEntity.movieName,
                rating = movieEntity.rating,
                poster = movieEntity.poster,
                overview = movieEntity.poster,
                backdropPoster = movieEntity.backdropPoster,
                genres = movieEntity.genres,
                homePageUrl = movieEntity.homePageUrl
            )
        }
    }
}