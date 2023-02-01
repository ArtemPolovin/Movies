package com.sacramento.data.apimodels.movie_state

data class MovieAccountStateApiModel(
    val favorite: Boolean?,
    val id: Int?,
    val rated: Boolean?,
    val watchlist: Boolean?
)