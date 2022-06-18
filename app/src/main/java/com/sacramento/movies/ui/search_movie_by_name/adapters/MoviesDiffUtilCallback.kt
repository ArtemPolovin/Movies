package com.sacramento.movies.ui.search_movie_by_name.adapters

import androidx.recyclerview.widget.DiffUtil
import com.sacramento.domain.models.MovieModel

class MoviesDiffUtilCallback : DiffUtil.ItemCallback<MovieModel>() {

    override fun areItemsTheSame(
        oldItem: MovieModel,
        newItem: MovieModel
    ): Boolean {
        return oldItem.movieId == newItem.movieId
    }

    override fun areContentsTheSame(
        oldItem: MovieModel,
        newItem: MovieModel
    ): Boolean {
        return oldItem == newItem
    }

}