package com.example.domain.models

data class MoviesSortedByGenreContainerModel(
    val genreId: String,
    val genreName: String,
    val moviesList: List<MovieModel>
)