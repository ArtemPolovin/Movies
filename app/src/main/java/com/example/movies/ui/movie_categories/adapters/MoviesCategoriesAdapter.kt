package com.example.movies.ui.movie_categories.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.MovieCategoryModel
import com.example.movies.databinding.MovieCategoryCellBinding

class MoviesCategoriesAdapter :
    RecyclerView.Adapter<MoviesCategoriesAdapter.MovieCategoriesViewHolder>() {

    private val movieCategoriesList = mutableListOf<MovieCategoryModel>()

    private val _category = MutableLiveData<MovieCategoryModel>()
    val movieCategory: LiveData<MovieCategoryModel> get() = _category

    fun setUpList(newList: List<MovieCategoryModel>) {
        movieCategoriesList.clear()
        movieCategoriesList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieCategoriesViewHolder {
        val binding = MovieCategoryCellBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MovieCategoriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieCategoriesViewHolder, position: Int) {
        val movieCellModel = movieCategoriesList[position]

        holder.bind(movieCellModel)
        holder.onClick(movieCellModel)
    }

    override fun getItemCount(): Int {
        return movieCategoriesList.size
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