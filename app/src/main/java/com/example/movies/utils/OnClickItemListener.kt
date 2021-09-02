package com.example.movies.utils

import com.example.domain.models.MovieWithDetailsModel

interface OnClickAdapterPopularMovieListener {
    fun getPopularMovie(movie: MovieWithDetailsModel)
}

interface OnLongClickAdapterItme {
    fun getMoviePositionAndId(adapterPosition: Int, movieId: List<Int>)
}