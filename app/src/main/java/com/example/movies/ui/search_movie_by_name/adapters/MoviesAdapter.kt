package com.example.movies.ui.search_movie_by_name.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.models.MovieModel
import com.example.movies.R
import com.example.movies.databinding.CellMovieByNameBinding

class MoviesAdapter : PagingDataAdapter<MovieModel, MoviesAdapter.MoviesViewHolder>(
    MoviesDiffUtilCallback()
) {

    var onItemClickLister: ((movieId: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val binding: CellMovieByNameBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.cell_movie_by_name, parent, false
        )
        return MoviesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        getItem(position)?.let { movieModel ->
            holder.bind(movieModel)
            holder.itemView.setOnClickListener {
                onItemClickLister?.invoke(movieModel.movieId)
            }
        }


    }

    class MoviesViewHolder(
        private val binding: CellMovieByNameBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movieModel: MovieModel) {
           binding.movieBindingModel = movieModel
        }

    }
}