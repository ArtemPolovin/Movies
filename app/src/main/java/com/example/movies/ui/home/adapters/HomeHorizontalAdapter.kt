package com.example.movies.ui.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.models.MovieModel
import com.example.movies.R
import kotlinx.android.synthetic.main.cell_home_horizontal.view.*

class HomeHorizontalAdapter : RecyclerView.Adapter<HomeHorizontalAdapter.HorizontalRVViewHolder>() {

    private val moviesList = mutableListOf<MovieModel>()

    private lateinit var onItemClickListener: OnItemClickListener

    fun setData(newList: List<MovieModel>) {
        moviesList.clear()
        moviesList.addAll(newList)
        notifyDataSetChanged()
    }

    fun onItemClick(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalRVViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cell_home_horizontal, parent, false)
        return HorizontalRVViewHolder(view, parent.context, onItemClickListener)
    }

    override fun onBindViewHolder(holder: HorizontalRVViewHolder, position: Int) {
        val movieModel = moviesList[position]

        holder.bind(movieModel)
        holder.onClickItem(movieModel.movieId)
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }

    inner class HorizontalRVViewHolder(
        itemView: View,
        private val context: Context,
        private val onItemClickListener: OnItemClickListener
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(movieModel: MovieModel) {
            itemView.apply {
                loadImage(image_horizontal_item, movieModel.poster)
                text_movie_name.text = movieModel.title
            }
        }

        fun loadImage(image: ImageView, imageUrl: String?) {
            Glide.with(context)
                .load(imageUrl)
                .into(image)
        }

        fun onClickItem(movieId: Int) {
            itemView.setOnClickListener {
                onItemClickListener.getClickedMovieId(movieId)
            }
        }

    }

    interface OnItemClickListener {
        fun getClickedMovieId(movieId: Int)
    }
}
