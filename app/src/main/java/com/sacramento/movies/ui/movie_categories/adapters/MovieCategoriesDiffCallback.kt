package com.sacramento.movies.ui.movie_categories.adapters

import androidx.recyclerview.widget.DiffUtil
import com.sacramento.domain.models.MovieCategoryModel

class MovieCategoriesDiffCallback: DiffUtil.ItemCallback<MovieCategoryModel>() {
    override fun areItemsTheSame(
        oldItem: MovieCategoryModel,
        newItem: MovieCategoryModel
    ): Boolean {
       return newItem.categoryName == oldItem.categoryName
    }

    override fun areContentsTheSame(
        oldItem: MovieCategoryModel,
        newItem: MovieCategoryModel
    ): Boolean {
        return newItem == oldItem
    }
}