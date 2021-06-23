package com.example.data.repositories_impl

import com.example.data.apimodels.movie_details.MovieDetailsModelApi
import com.example.data.mapers.MoviesApiMapper
import com.example.data.network.MoviesApi
import com.example.domain.models.PopularMovieWithDetailsModel
import com.example.domain.repositories.MoviesRepository


class MoviesRepositoryImpl(
    private val moviesApi: MoviesApi,
    private val moviesApiMapper: MoviesApiMapper
    ):MoviesRepository  {

   override  suspend fun getMoviesWithDetails(page: Int): List<PopularMovieWithDetailsModel> {
        val movieResponse = moviesApi.getPopularMovies(page).results
        val movieDetails: List<MovieDetailsModelApi> = moviesApi.getPopularMovies(page).results.map {
            moviesApi.getMoviesDetails(it.id)
        }
        return moviesApiMapper.mapMovieDetailsAndMoviesToModelList(movieDetails,movieResponse)
    }

}