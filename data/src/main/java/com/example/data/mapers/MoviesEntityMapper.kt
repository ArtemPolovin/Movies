package com.example.data.mapers

import com.example.data.db.tables.movie_tables.SavedMovieEntity
import com.example.domain.models.MovieWithDetailsModel

class MoviesEntityMapper {

    //This function maps MovieWithDetailsModel to Entity model
    fun mapMovieModelToEntity(movie: MovieWithDetailsModel): SavedMovieEntity {
        return SavedMovieEntity(
            movieId = movie.id,
            releaseData = movie.releaseData,
            popularityScore = movie.popularityScore,
            movieName = movie.movieName,
            rating = movie.rating,
            poster = movie.poster,
            overview = movie.overview,
            backdropPoster = movie.backdropPoster,
            genres = movie.genres,
            homePageUrl = movie.homePageUrl,
            video = movie.video,
            vote_count = movie.voteCount
        )
    }

    //This function takes all saved movies from DB and maps the list to model list
    fun mapMovieEntityListToModelList(movieEntityList: List<SavedMovieEntity>):List<MovieWithDetailsModel> {

        return movieEntityList.map { movieEntity ->
            MovieWithDetailsModel(
                id = movieEntity.movieId,
                releaseData = movieEntity.releaseData,
                popularityScore = movieEntity.popularityScore,
                movieName = movieEntity.movieName,
                rating = movieEntity.rating,
                poster = movieEntity.poster,
                overview = movieEntity.overview,
                backdropPoster = movieEntity.backdropPoster,
                genres = movieEntity.genres,
                homePageUrl = movieEntity.homePageUrl,
                video = movieEntity.video,
                voteCount = movieEntity.vote_count
            )
        }
    }
}