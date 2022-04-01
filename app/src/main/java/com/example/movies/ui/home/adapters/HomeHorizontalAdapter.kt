package com.example.movies.ui.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.MovieModel
import com.example.movies.R
import com.example.movies.databinding.CellMovieHorizontalListBinding

class HomeHorizontalAdapter : ListAdapter<MovieModel, HomeHorizontalAdapter.HorizontalRVViewHolder>(MovieDiffCallback()) {

    var onItemClickListener: ((movieId: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalRVViewHolder {
        val binding: CellMovieHorizontalListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.cell_movie_horizontal_list,
            parent,
            false
        )
        return HorizontalRVViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HorizontalRVViewHolder, position: Int) {
        val movieModel = getItem(position)

        holder.bind(movieModel)
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(movieModel.movieId)
        }
    }

    class HorizontalRVViewHolder(
        binding: CellMovieHorizontalListBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

       private val movieDataBinding = binding

        fun bind(movieModel: MovieModel) {
            movieDataBinding.movieBindingModel = movieModel
        }
    }

}
