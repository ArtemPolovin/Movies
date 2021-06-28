package com.example.movies.ui.saved_movie

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.models.PopularMovieWithDetailsModel
import com.example.movies.R
import com.example.movies.utils.OnClickAdapterPopularMovieListener
import kotlinx.android.synthetic.main.cell_movie.view.*

class SavedMovieAdapter : RecyclerView.Adapter<SavedMovieAdapter.SavedMoviesViewHolder>() {

    private val savedMoviesList = mutableListOf<PopularMovieWithDetailsModel>()

    private lateinit var onclickAdapterPopularMovieListener: OnClickAdapterPopularMovieListener

    fun onclick(onclickAdapterPopularMovieListener: OnClickAdapterPopularMovieListener) {
        this.onclickAdapterPopularMovieListener = onclickAdapterPopularMovieListener
    }

    fun setupList(newSavedMovieList: List<PopularMovieWithDetailsModel>) {
        savedMoviesList.clear()
        savedMoviesList.addAll(newSavedMovieList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedMoviesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cell_saved_movie, parent, false)
        return SavedMoviesViewHolder(view, parent.context, onclickAdapterPopularMovieListener)
    }

    override fun onBindViewHolder(holder: SavedMoviesViewHolder, position: Int) {

        val movieModel = savedMoviesList[position]
        holder.itemView.apply {
            holder.loadImage(image_movie_poster, movieModel.poster)
            text_movie_name.text = movieModel.movieName
            text_rating.text = movieModel.rating.toString()
        }

        holder.click(movieModel)
    }

    override fun getItemCount(): Int {
        return savedMoviesList.size
    }

    inner class SavedMoviesViewHolder(
        itemView: View,
        private val context: Context,
        private val onclickAdapterPopularMovieListener: OnClickAdapterPopularMovieListener
    ) : RecyclerView.ViewHolder(itemView) {

        fun loadImage(image: ImageView, imageUrl: String?) {
            Glide.with(context)
                .load(imageUrl)
                .into(image)
        }

        fun click(movieModel: PopularMovieWithDetailsModel) {
            itemView.setOnClickListener {
                onclickAdapterPopularMovieListener.getPopularMovie(movieModel)
            }
        }
    }


}