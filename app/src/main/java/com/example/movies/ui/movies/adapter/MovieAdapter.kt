package com.example.movies.ui.movies.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.models.MovieWithDetailsModel
import com.example.movies.databinding.CellMovieBinding
import com.example.movies.utils.DATA_VIEWTYPE
import com.example.movies.utils.LOADSTATE_VIEW_TYPE

class MovieAdapter :
    PagingDataAdapter<MovieWithDetailsModel, MovieAdapter.MoviesViewHolder>(MovieDiffUtilCallback()) {

    private lateinit var onClickAdapterPopularMovieListener: OnClickAdapterPopularMovieListener

    fun onClickItem(onClickAdapterPopularMovieListener: OnClickAdapterPopularMovieListener) {
        this.onClickAdapterPopularMovieListener = onClickAdapterPopularMovieListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val binding = CellMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MoviesViewHolder(binding, parent.context, onClickAdapterPopularMovieListener)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) {
            DATA_VIEWTYPE
        } else {
            LOADSTATE_VIEW_TYPE
        }
    }

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

        fun bind(popularMovie: MovieWithDetailsModel) {
          loadImage(binding.imageMoviePoster, popularMovie.poster)
            binding.textMovieName.text = popularMovie.movieName
            binding.textPopularity.text = popularMovie.popularityScore
            binding.textReleaseDate.text = popularMovie.releaseData
            binding.textRating.text = popularMovie.rating.toString()
            popularMovie.rating?.let {
                binding.ratingBarMovie.rating = it.toFloat()
            }
            binding.textGenre.text = popularMovie.genres
            binding.textVoteCount.text = "(${popularMovie.voteCount})"
        }

        fun onClick(movieWithDetailsModel: MovieWithDetailsModel) {
            itemView.setOnClickListener {
                onClickAdapterPopularMovieListener.getMovie(movieWithDetailsModel)
            }
        }

        fun loadImage(image: ImageView, imageUrl: String?) {
            Glide.with(context)
                .load(imageUrl)
                .into(image)
        }

    }

    class MovieDiffUtilCallback : DiffUtil.ItemCallback<MovieWithDetailsModel>() {
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
            return oldItem.id == newItem.id
        }

    }

    interface OnClickAdapterPopularMovieListener {
        fun getMovie(movie: MovieWithDetailsModel)
    }

}



