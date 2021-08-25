package com.example.movies.utils

import com.example.domain.models.PopularMovieWithDetailsModel

interface OnClickAdapterPopularMovieListener {
    fun getPopularMovie(movie: PopularMovieWithDetailsModel)
}

interface OnLongClickAdapterItme {
    fun getMoviePositionAndId(adapterPosition: Int, movieId: List<Int>)
}