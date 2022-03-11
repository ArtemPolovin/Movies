package com.example.movies.ui.search_movie_by_name.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.models.MovieModel
import com.example.movies.databinding.CellMovieByNameBinding
import com.example.movies.databinding.CellMovieHorizontalListBinding

class MoviesAdapter : PagingDataAdapter<MovieModel, MoviesAdapter.MoviesViewHolder>(
    MoviesDiffUtilCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val binding = CellMovieByNameBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MoviesViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        getItem(position)?.let { movieModel ->
            holder.bind(movieModel)
        }


    }

    class MoviesViewHolder(
        private val binding: CellMovieByNameBinding,
        private val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movieModel: MovieModel) {
            itemView.apply {
                loadImage(binding.movieByNameCell.imageHorizontalItem, movieModel.poster ?: "")
                binding.movieByNameCell.textMovieName.setPadding(0,0,16,0)
                binding.movieByNameCell.textMovieName.text = movieModel.title
            }
        }

        private fun loadImage(image: ImageView, imageUrl: String) {
            Glide.with(context)
                .load(imageUrl)
                .into(image)
        }

    }
}