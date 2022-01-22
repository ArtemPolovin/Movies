package com.example.movies.ui.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.domain.models.MoviePosterViewPagerModel
import com.example.movies.R
import kotlinx.android.synthetic.main.cell_movie_poster_view_pager.view.*

class HomePosterViewPagerAdapter(
    private val postersList: MutableList<MoviePosterViewPagerModel>,
    private val viewPager: ViewPager2
    ) : RecyclerView.Adapter<HomePosterViewPagerAdapter.PosterViewHolder>() {

//    private val postersListSize = postersList.size
//    private var countOfPosters = 1

    var onPosterClickListener: ((movieId:Int) -> Unit)? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cell_movie_poster_view_pager, parent, false)
        return PosterViewHolder(view, parent.context)
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

    inner class PosterViewHolder(viewItem: View, private val context: Context) :
        RecyclerView.ViewHolder(viewItem) {

        fun bind(moviePosterModel: MoviePosterViewPagerModel, position: Int) {

            itemView.apply {
                text_movie_name.text = moviePosterModel.movieName
                text_movie_genres.text = moviePosterModel.genreName
//                text_count_of_posters.text = String.format(
//                    context.applicationContext.getString(R.string.count_of_poster),
//                    countOfPosters++,
//                    postersListSize
//                )
                loadImage(image_poster,moviePosterModel.poster)
            }
        }

        fun loadImage(image: ImageView, imageUrl: String?) {
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

    private val run = Runnable{
        postersList.addAll(postersList)
        notifyDataSetChanged()
    }

}