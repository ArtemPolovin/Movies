package com.example.movies.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.models.PopularMovieModel
import com.example.movies.R
import kotlinx.android.synthetic.main.cell_movie.view.*

class MoviesAdapter: RecyclerView.Adapter<MoviesAdapter.MoviesHolder>() {

    private val moviesList = mutableListOf<PopularMovieModel>()

    fun setupMoveList(newMovieList: List<PopularMovieModel>) {
        moviesList.clear()
        moviesList.addAll(newMovieList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_movie, parent, false)
        return MoviesHolder(view,parent.context)
    }

    override fun onBindViewHolder(holder: MoviesHolder, position: Int) {
        val popularMovie = moviesList[position]
        holder.itemView.apply {
            holder.loadImage(image_movie_poster, popularMovie.poster)
            text_movie_name.text = popularMovie.movieName
            text_popularity.text = popularMovie.popularityScore
            text_release_date.text = popularMovie.releaseData
        }
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }

    inner class MoviesHolder(itemView: View, private val context: Context) : RecyclerView.ViewHolder(itemView) {

        fun loadImage(image: ImageView, imageUrl: String?) {
            Glide.with(context)
                .load(imageUrl)
                .into(image)
        }
    }
}