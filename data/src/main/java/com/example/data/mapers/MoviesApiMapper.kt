package com.example.data.mapers

import com.example.data.apimodels.movie_details.Genre
import com.example.data.apimodels.movie_details.MovieDetailsModelApi
import com.example.data.apimodels.movies.Result
import com.example.data.utils.POSTER_BASE_URL
import com.example.domain.models.MovieWithDetailsModel

class MoviesApiMapper {

    fun mapMovieDetailsAndMoviesToModelList(
        movieDetailsList: List<MovieDetailsModelApi>,
        moviesIdList: List<Result>
    ): List<MovieWithDetailsModel> {

        val movieWithDetails = mutableListOf<MovieWithDetailsModel>()

        moviesIdList.forEach { movieId ->
            movieDetailsList.forEach { movieDetails ->
                if (movieId.id == movieDetails.id) {
                    movieWithDetails.add(
                        MovieWithDetailsModel(
                            releaseData = movieDetails.release_date,
                            popularityScore = movieDetails.popularity.toString(),
                            movieName = movieDetails.title,
                            rating = movieDetails.vote_average,
                            poster = "${POSTER_BASE_URL}${movieDetails.poster_path}",
                            backdropPoster = "${POSTER_BASE_URL}${movieDetails.backdrop_path}",
                            overview = movieDetails.overview,
                            genres = movieDetails.genres?.joinToString(", ") { it.name ?: "" },
                            homePageUrl = movieDetails.homepage,
                            id = movieDetails.id
                        )
                    )
                }

            }
        }
        return movieWithDetails
    }

}