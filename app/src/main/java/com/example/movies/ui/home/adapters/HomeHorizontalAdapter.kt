package com.example.movies.ui.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.models.MovieModel
import com.example.movies.databinding.CellMovieHorizontalListBinding

class HomeHorizontalAdapter : RecyclerView.Adapter<HomeHorizontalAdapter.HorizontalRVViewHolder>() {

    private val moviesList = mutableListOf<MovieModel>()

    var onItemClickListener: ((movieId: Int) -> Unit)? = null

    fun setData(newList: List<MovieModel>) {
        moviesList.clear()
        moviesList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalRVViewHolder {
        val binding = CellMovieHorizontalListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HorizontalRVViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: HorizontalRVViewHolder, position: Int) {
        val movieModel = moviesList[position]

        holder.bind(movieModel)
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(movieModel.movieId)
        }
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }

    class HorizontalRVViewHolder(
        private val binding: CellMovieHorizontalListBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movieModel: MovieModel) {
            itemView.apply {
                loadImage(binding.imageHorizontalItem, movieModel.poster)
                binding.textMovieName.text = movieModel.title
            }
        }

        private fun loadImage(image: ImageView, imageUrl: String?) {
            Glide.with(context)
                .load(imageUrl)
                .into(image)
        }
    }

}
