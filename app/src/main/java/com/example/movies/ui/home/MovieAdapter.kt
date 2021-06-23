package com.example.movies.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.models.PopularMovieWithDetailsModel
import com.example.movies.R
import com.example.movies.utils.DATA_VIEWTYPE
import com.example.movies.utils.LOADSTATE_VIEW_TYPE
import kotlinx.android.synthetic.main.cell_movie.view.*

class MovieAdapter : PagingDataAdapter<PopularMovieWithDetailsModel, MovieAdapter.MoviesViewHolder>(
    MovieDiffUtilCallback()
) {

    private lateinit var onClickMovieItemListener: OnclickMovieItemListener

    fun onClickItem(onClickMovieItemListener: OnclickMovieItemListener) {
        this.onClickMovieItemListener = onClickMovieItemListener
    }

    class MoviesViewHolder(
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

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
         getItem(position)?.let {popularMovie ->

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

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_movie, parent, false)
        return MoviesViewHolder(view, parent.context, onClickMovieItemListener)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) {
            DATA_VIEWTYPE
        } else {
            LOADSTATE_VIEW_TYPE
        }
    }

    class MovieDiffUtilCallback : DiffUtil.ItemCallback<PopularMovieWithDetailsModel>() {
        override fun areItemsTheSame(
            oldItem: PopularMovieWithDetailsModel,
            newItem: PopularMovieWithDetailsModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: PopularMovieWithDetailsModel,
            newItem: PopularMovieWithDetailsModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

    }

    interface OnclickMovieItemListener {
        fun getMovieModel(movieWithDetailsModel: PopularMovieWithDetailsModel)
    }

}


