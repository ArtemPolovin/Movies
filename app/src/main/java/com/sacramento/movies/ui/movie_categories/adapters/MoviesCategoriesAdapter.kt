package com.sacramento.movies.ui.movie_categories.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sacramento.domain.models.MovieCategoryModel
import com.sacramento.movies.databinding.MovieCategoryCellBinding

/*class MoviesCategoriesAdapter :
    RecyclerView.Adapter<MoviesCategoriesAdapter.MovieCategoriesViewHolder>() {*/

class MoviesCategoriesAdapter :
    ListAdapter<MovieCategoryModel, MoviesCategoriesAdapter.MovieCategoriesViewHolder>(
        MovieCategoriesDiffCallback()
    ) {

    private val _category = MutableLiveData<MovieCategoryModel>()
    val movieCategory: LiveData<MovieCategoryModel> get() = _category

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieCategoriesViewHolder {
        val binding = MovieCategoryCellBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MovieCategoriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieCategoriesViewHolder, position: Int) {
        val movieCellModel = getItem(position)

        holder.bind(movieCellModel)
        holder.onClick(movieCellModel)
    }

    inner class MovieCategoriesViewHolder(private val binding: MovieCategoryCellBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movieMovieCategoryModel: MovieCategoryModel) {
            itemView.apply {
                binding.imageMovieCategory.setImageResource(movieMovieCategoryModel.image)
                binding.textTitle.text = movieMovieCategoryModel.categoryName
            }

        }

        fun onClick(movieMovieCategoryModel: MovieCategoryModel) {
            binding.cardView.setOnClickListener {
                _category.value = movieMovieCategoryModel
            }
        }
    }
}