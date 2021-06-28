package com.example.data.mapers

import com.example.data.apimodels.movie_details.MovieDetailsModelApi
import com.example.data.apimodels.movies.Result
import com.example.data.utils.POSTER_BASE_URL
import com.example.domain.models.PopularMovieWithDetailsModel

class MoviesApiMapper {

    fun mapMovieDetailsAndMoviesToModelList(
        movieDetailsList: List<MovieDetailsModelApi>,
        popularMoviesIdList: List<Result>
    ): List<PopularMovieWithDetailsModel> {

        val movieWithDetails = mutableListOf<PopularMovieWithDetailsModel>()

        popularMoviesIdList.forEach { popularMovie ->
            movieDetailsList.forEach { movieDetails ->
                if (popularMovie.id == movieDetails.id) {
                    movieWithDetails.add(
                        PopularMovieWithDetailsModel(
                            releaseData = movieDetails.release_date,
                            popularityScore = movieDetails.popularity.toString(),
                            movieName = movieDetails.original_title,
                            rating = movieDetails.vote_average,
                            poster = "${POSTER_BASE_URL}${movieDetails.poster_path}",
                            backdropPoster = "${POSTER_BASE_URL}${movieDetails.backdrop_path}",
                            overview = movieDetails.overview,
                            genres = movieDetails.genres.joinToString(", ") { it.name },
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