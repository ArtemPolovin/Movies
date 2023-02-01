package com.sacramento.movies.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.sacramento.domain.models.MoviePosterViewPagerModel
import com.sacramento.movies.R
import com.sacramento.movies.databinding.CellMoviePosterViewPagerBinding

class HomePosterViewPagerAdapter(
    private val postersList: MutableList<MoviePosterViewPagerModel>,
    private val viewPager: ViewPager2
) : RecyclerView.Adapter<HomePosterViewPagerAdapter.PosterViewHolder>() {

    var onPosterClickListener: ((movieId: Int) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterViewHolder {
        val binding: CellMoviePosterViewPagerBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.cell_movie_poster_view_pager,
            parent,
            false
        )
        return PosterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PosterViewHolder, position: Int) {
        val moviePosterViewPagerModel = postersList[position]

        holder.bind(moviePosterViewPagerModel)
        holder.onPosterClick(moviePosterViewPagerModel.movieId)

        if (position == postersList.size - 2) {
            viewPager.post(run)
        }

    }

    override fun getItemCount(): Int {
        return postersList.size
    }

    inner class PosterViewHolder(
        binding: CellMoviePosterViewPagerBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val posterBinding = binding

        fun bind(moviePosterModel: MoviePosterViewPagerModel) {
            posterBinding.posterModel = moviePosterModel
        }

        fun onPosterClick(movieId: Int) {
            itemView.setOnClickListener {
                onPosterClickListener?.invoke(movieId)
            }

        }
    }

    private val run = Runnable {
        postersList.addAll(postersList)
        notifyDataSetChanged()
    }

}