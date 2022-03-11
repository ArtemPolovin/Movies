package com.example.movies.ui.search_movie_by_name.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.models.MovieModel
import com.example.domain.models.MovieWithDetailsModel

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