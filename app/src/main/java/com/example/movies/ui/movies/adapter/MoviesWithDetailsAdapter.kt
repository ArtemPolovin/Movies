package com.example.movies.ui.movies.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.models.MovieWithDetailsModel
import com.example.movies.R
import com.example.movies.databinding.CellMovieBinding

class MoviesWithDetailsAdapter :
    PagingDataAdapter<MovieWithDetailsModel, MoviesWithDetailsAdapter.MoviesViewHolder>(
        MoviesWithDetailsDiffUtilCallback()
    ) {

    private lateinit var onClickAdapterPopularMovieListener: OnClickAdapterPopularMovieListener

    fun onClickItem(onClickAdapterPopularMovieListener: OnClickAdapterPopularMovieListener) {
        this.onClickAdapterPopularMovieListener = onClickAdapterPopularMovieListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val binding: CellMovieBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.cell_movie,
            parent,
            false
        )
        return MoviesViewHolder(binding, parent.context, onClickAdapterPopularMovieListener)
    }

    /*override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) {
            DATA_VIEWTYPE
        } else {
            LOADSTATE_VIEW_TYPE
        }
    }*/

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        getItem(position)?.let { popularMovie ->
            holder.bind(popularMovie)
            holder.onClick(popularMovie)
        }

    }

    class MoviesViewHolder(
        private val binding: CellMovieBinding,
        private val context: Context,
        private val onClickAdapterPopularMovieListener: OnClickAdapterPopularMovieListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movieWithDetailsModel: MovieWithDetailsModel) {
            binding.movieWithDetailsBindingModel = movieWithDetailsModel
        }

        fun onClick(movieWithDetailsModel: MovieWithDetailsModel) {
            itemView.setOnClickListener {
                onClickAdapterPopularMovieListener.getMovie(movieWithDetailsModel)
            }
        }
    }

    class MoviesWithDetailsDiffUtilCallback : DiffUtil.ItemCallback<MovieWithDetailsModel>() {
        override fun areItemsTheSame(
            oldItem: MovieWithDetailsModel,
            newItem: MovieWithDetailsModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: MovieWithDetailsModel,
            newItem: MovieWithDetailsModel
        ): Boolean {
            return oldItem == newItem
        }

    }

    interface OnClickAdapterPopularMovieListener {
        fun getMovie(movie: MovieWithDetailsModel)
    }

}



