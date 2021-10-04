package com.example.movies.ui.movie_categories

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.CategoryModel
import com.example.movies.R
import kotlinx.android.synthetic.main.movie_category_cell.view.*

class MoviesCategoriesAdapter :
    RecyclerView.Adapter<MoviesCategoriesAdapter.MovieCategoriesViewHolder>() {

    private val movieCategoriesList = mutableListOf<CategoryModel>()

    private val _category = MutableLiveData<CategoryModel>()
    val category: LiveData<CategoryModel>get() = _category

    fun setUpList(newList: List<CategoryModel>) {
        movieCategoriesList.clear()
        movieCategoriesList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieCategoriesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.movie_category_cell, parent, false)
        return MovieCategoriesViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: MovieCategoriesViewHolder, position: Int) {
        val movieCellModel = movieCategoriesList[position]

        holder.bind(movieCellModel)
        holder.onClick(movieCellModel)
    }

    override fun getItemCount(): Int {
        return movieCategoriesList.size
    }

    inner class MovieCategoriesViewHolder(itemView: View, private val context: Context) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(movieCategoryModel: CategoryModel) {
            itemView.apply {
                image_movie_category.setImageResource(movieCategoryModel.image)
                text_title.text = movieCategoryModel.categoryName
            }

        }

        fun onClick(movieCategoryModel: CategoryModel) {
            itemView.card_view.setOnClickListener {
                _category.value = movieCategoryModel
            }
        }
    }
}