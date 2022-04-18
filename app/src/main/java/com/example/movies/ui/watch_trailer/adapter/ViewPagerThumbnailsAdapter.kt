package com.example.movies.ui.watch_trailer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.domain.models.TrailerModel
import com.example.movies.R
import com.example.movies.databinding.CellMovieHorizontalListBinding
import com.example.movies.databinding.CellThumbnailBinding

class ViewPagerThumbnailsAdapter :
    ListAdapter<TrailerModel, ViewPagerThumbnailsAdapter.ThumbnailsViewHolder>(
        ViewPagerThumbnailsDiffCallback()
    ) {

    var onItemClickListener: ((videoKey: String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThumbnailsViewHolder {
        val binding =
            CellThumbnailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ThumbnailsViewHolder(binding,parent.context)

    }

    override fun onBindViewHolder(holder: ThumbnailsViewHolder, position: Int) {
        val trailerModel = getItem(position)

        holder.bind(trailerModel)

        holder.image.setOnClickListener {
            trailerModel.videoKey?.let {
                onItemClickListener?.invoke(it)
            }
        }
    }

    class ThumbnailsViewHolder(private val binding: CellThumbnailBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        val image = binding.imageVideo

        fun bind(trailerModel: TrailerModel) {
            trailerModel.apply {
                loadImage(binding.imageVideo,thumbnailUrl)
            }

        }

      private  fun loadImage(image: ImageView, imageUrl: String?) {
            Glide.with(context)
                .load(imageUrl)
                .into(image)

        }

    }
}