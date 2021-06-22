package com.example.domain.repositories

import com.example.domain.models.PopularMovieWithDetailsModel
import io.reactivex.Single

interface MoviesRepository {
    //fun getMoviesWithDetails(list: List<Int>): Single<List<PopularMovieWithDetails>>
    fun getMoviesWithDetails() :Single<List<PopularMovieWithDetailsModel>>
}