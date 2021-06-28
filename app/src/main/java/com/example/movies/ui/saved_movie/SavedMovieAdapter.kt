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
import kotlinx.android.synthetic.main.cell_movie.view.*

class SavedMovieAdapter: RecyclerView.Adapter<SavedMovieAdapter.SavedMoviesViewHolder>() {

    private val savedMoviesList = mutableListOf<PopularMovieWithDetailsModel>()

    fun setupList(newSavedMovieList: List<PopularMovieWithDetailsModel>) {
        savedMoviesList.clear()
        savedMoviesList.addAll(newSavedMovieList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedMoviesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cell_saved_movie, parent, false)
        return SavedMoviesViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: SavedMoviesViewHolder, position: Int) {

        val movieModel = savedMoviesList[position]
        holder.itemView.apply {
            holder.loadImage(image_movie_poster, movieModel.poster)
            text_movie_name.text = movieModel.movieName
//            text_popularity.text = movieModel.popularityScore
          //  text_release_date.text = movieModel.releaseData
            text_rating.text = movieModel.rating.toString()
          //  rating_bar_movie.rating = movieModel.rating.toFloat()
          //  text_genre.text = movieModel.genres
        }
    }

    override fun getItemCount(): Int {
       return savedMoviesList.size
    }

    inner class SavedMoviesViewHolder(itemView: View, private val context: Context) : RecyclerView.ViewHolder(itemView) {

        fun loadImage(image: ImageView, imageUrl: String?) {
            Glide.with(context)
                .load(imageUrl)
                .into(image)
        }
    }
}