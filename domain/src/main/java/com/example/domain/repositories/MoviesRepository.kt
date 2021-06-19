package com.example.domain.repositories

import com.example.domain.models.PopularMovieModel
import io.reactivex.Single

interface MoviesRepository {
    fun getPopularMovies():Single<List<PopularMovieModel>>
}