package com.example.domain.repositories

import com.example.domain.models.PopularMovieWithDetailsModel

interface MoviesRepository {

    suspend fun getMoviesWithDetails(page: Int) : List<PopularMovieWithDetailsModel>
}