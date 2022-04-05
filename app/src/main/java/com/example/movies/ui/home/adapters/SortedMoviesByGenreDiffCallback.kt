package com.example.movies.ui.home.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.models.MoviesSortedByGenreContainerModel

class SortedMoviesByGenreDiffCallback: DiffUtil.ItemCallback<MoviesSortedByGenreContainerModel>() {
    override fun areItemsTheSame(
        oldItem: MoviesSortedByGenreContainerModel,
        newItem: MoviesSortedByGenreContainerModel
    ): Boolean {
       return oldItem.genreId == newItem.genreId
    }

    override fun areContentsTheSame(
        oldItem: MoviesSortedByGenreContainerModel,
        newItem: MoviesSortedByGenreContainerModel
    ): Boolean {
        return oldItem == newItem
    }
}