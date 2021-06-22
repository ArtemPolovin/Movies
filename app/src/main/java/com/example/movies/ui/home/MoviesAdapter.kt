package com.example.movies.ui.home

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

class MoviesAdapter : RecyclerView.Adapter<MoviesAdapter.MoviesHolder>() {

    private val moviesList = mutableListOf<PopularMovieWithDetailsModel>()

    private lateinit var onClickMovieItemListener: OnclickMovieItemListener

    fun onClickItem(onClickMovieItemListener: OnclickMovieItemListener) {
        this.onClickMovieItemListener = onClickMovieItemListener
    }

    fun setupMoveList(newMovieList: List<PopularMovieWithDetailsModel>) {
        moviesList.clear()
        moviesList.addAll(newMovieList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_movie, parent, false)
        return MoviesHolder(view, parent.context, onClickMovieItemListener)
    }

    override fun onBindViewHolder(holder: MoviesHolder, position: Int) {
        val popularMovie = moviesList[position]
        holder.itemView.apply {
            holder.loadImage(image_movie_poster, popularMovie.poster)
            text_movie_name.text = popularMovie.movieName
            text_popularity.text = popularMovie.popularityScore
            text_release_date.text = popularMovie.releaseData
            text_rating.text = popularMovie.rating.toString()
            rating_bar_movie.rating = popularMovie.rating.toFloat()
            text_genre.text = popularMovie.genres
        }

        holder.onClick(popularMovie)
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }

    inner class MoviesHolder(
        itemView: View,
        private val context: Context,
        private val onClickMovieItemListener: OnclickMovieItemListener
    ) : RecyclerView.ViewHolder(itemView) {

        fun onClick(movieWithDetailsModel: PopularMovieWithDetailsModel) {
            itemView.setOnClickListener {
                onClickMovieItemListener.getMovieModel(movieWithDetailsModel)
            }
        }

        fun loadImage(image: ImageView, imageUrl: String?) {
            Glide.with(context)
                .load(imageUrl)
                .into(image)
        }
    }

    interface OnclickMovieItemListener {
        fun getMovieModel(movieWithDetailsModel: PopularMovieWithDetailsModel)
    }
}