package com.example.data.mapers

import com.example.data.apimodels.movie_details.Genre
import com.example.data.apimodels.movie_details.MovieDetailsModelApi
import com.example.data.apimodels.movies.Result
import com.example.data.apimodels.video.VideoApiModel
import com.example.data.utils.POSTER_BASE_URL
import com.example.domain.models.MovieWithDetailsModel

class MoviesApiMapper {

    fun mapMovieDetailsAndMoviesToModelList(
        movieDetailsList: List<MovieDetailsModelApi>,
        moviesIdList: List<Result>,
        videosList: List<VideoApiModel>
    ): List<MovieWithDetailsModel> {

        val movieWithDetails = mutableListOf<MovieWithDetailsModel>()

            movieDetailsList.forEach { movieDetails ->
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
                            video = getVideoKeyByMovieId(movieDetails.id,videosList),
                            id = movieDetails.id
                        )
                    )
            }
        return movieWithDetails
    }

    private fun getVideoKeyByMovieId(movieId: Int, videosList: List<VideoApiModel>): String {
        //return videosList.filter { it.id == movieId }[0]?.results[0]?.key
        for (video in videosList) {
            if (video.id == movieId) {
                if (video.results.isNotEmpty()) {
                    return video.results[0].key
                }

            }
        }
        return ""
    }

}