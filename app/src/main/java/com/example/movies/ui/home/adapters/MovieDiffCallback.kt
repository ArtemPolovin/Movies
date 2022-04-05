package com.example.movies.ui.home.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.models.MovieModel

class MovieDiffCallback : DiffUtil.ItemCallback<MovieModel>() {
    override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
       return oldItem.movieId == newItem.movieId
    }

    override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
       return oldItem == newItem
    }

}