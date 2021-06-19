package com.example.data.mapers

import com.example.data.apimodels.PopularMoviesApi
import com.example.data.utils.SMALL_POSTER_BASE_URL
import com.example.domain.models.PopularMovieModel

class MoviesApiMapper {

    fun mapPopularMoviesApiToModelList(moviesApi: PopularMoviesApi): List<PopularMovieModel> {
        return moviesApi.results.map {
            PopularMovieModel(
                genreList = it.genre_ids,
                releaseData = it.release_date,
                popularityScore = it.popularity.toString(),
                movieName = it.title,
                rating = it.vote_average,
                poster = "${SMALL_POSTER_BASE_URL}${it.poster_path}"
            )
        }
    }
}