package com.example.movies.ui.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.domain.models.MoviePosterViewPagerModel
import com.example.movies.databinding.CellMoviePosterViewPagerBinding

class HomePosterViewPagerAdapter(
    private val postersList: MutableList<MoviePosterViewPagerModel>,
    private val viewPager: ViewPager2
) : RecyclerView.Adapter<HomePosterViewPagerAdapter.PosterViewHolder>() {

    var onPosterClickListener: ((movieId: Int) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterViewHolder {
        val binding = CellMoviePosterViewPagerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PosterViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: PosterViewHolder, position: Int) {
        val moviePosterViewPagerModel = postersList[position]

        holder.bind(moviePosterViewPagerModel, position + 1)
        holder.onPosterClick(moviePosterViewPagerModel.movieId)

        if (position == postersList.size - 2) {
            viewPager.post(run)
        }

    }

    override fun getItemCount(): Int {
        return postersList.size
    }

    inner class PosterViewHolder(
        private val binding: CellMoviePosterViewPagerBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(moviePosterModel: MoviePosterViewPagerModel, position: Int) {

            itemView.apply {
                binding.textMovieName.text = moviePosterModel.movieName
                binding.textMovieGenres.text = moviePosterModel.genreName
//                text_count_of_posters.text = String.format(
//                    context.applicationContext.getString(R.string.count_of_poster),
//                    countOfPosters++,
//                    postersListSize
//                )
                loadImage(binding.imagePoster, moviePosterModel.poster)
            }
        }

       private fun loadImage(image: ImageView, imageUrl: String?) {
            Glide.with(context)
                .load(imageUrl)
                .into(image)
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