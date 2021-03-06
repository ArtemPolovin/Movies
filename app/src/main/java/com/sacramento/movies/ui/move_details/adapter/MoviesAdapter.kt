package com.sacramento.movies.ui.move_details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.sacramento.domain.models.MovieModel
import com.sacramento.movies.databinding.CellMovieHorizontalListBinding
import com.sacramento.movies.ui.home.adapters.HomeHorizontalAdapter

class MoviesAdapter :
    ListAdapter<MovieModel, HomeHorizontalAdapter.HorizontalRVViewHolder>(SimilarMoviesDiffCallback()) {

    var onItemClickListener: ((movieId: Int) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeHorizontalAdapter.HorizontalRVViewHolder {
        val binding = CellMovieHorizontalListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return HomeHorizontalAdapter.HorizontalRVViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HomeHorizontalAdapter.HorizontalRVViewHolder,
        position: Int
    ) {
        val movie = getItem(position)
        holder.bind(movie)
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(movie.movieId)
        }

    }

}