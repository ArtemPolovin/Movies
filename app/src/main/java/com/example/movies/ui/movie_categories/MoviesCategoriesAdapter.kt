package com.example.movies.ui.movie_categories

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.models.MovieCategoriesCellModel
import com.example.movies.R
import kotlinx.android.synthetic.main.movie_category_cell.view.*

class MoviesCategoriesAdapter :
    RecyclerView.Adapter<MoviesCategoriesAdapter.MovieCategoriesViewHolder>() {

    private val movieCategoriesList = mutableListOf<MovieCategoriesCellModel>()

    private val _category = MutableLiveData<String>()
    val category: LiveData<String>get() = _category

    fun setUpList(newList: List<MovieCategoriesCellModel>) {
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

        fun bind(movieCategoryModel: MovieCategoriesCellModel) {
            itemView.apply {
                loadImage(image_movie_category, movieCategoryModel.image)
                text_title.text = movieCategoryModel.category
            }

        }

        fun loadImage(image: ImageView, imageUrl: String?) {
            Glide.with(context)
                .load(imageUrl)
                .into(image)
        }

        fun onClick(movieCategoryModel: MovieCategoriesCellModel) {
            itemView.card_view.setOnClickListener {
                _category.value = movieCategoryModel.category
            }
        }
    }
}