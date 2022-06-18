package com.sacramento.movies.ui.watch_trailer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sacramento.domain.models.TrailerModel
import com.sacramento.movies.databinding.CellThumbnailBinding

class ViewPagerThumbnailsAdapter(var onItemClickListener: ((videoKey: String) -> Unit)? = null) :
    ListAdapter<TrailerModel, ViewPagerThumbnailsAdapter.ThumbnailsViewHolder>(
        ViewPagerThumbnailsDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThumbnailsViewHolder {
        val binding =
            CellThumbnailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ThumbnailsViewHolder(binding,parent.context, onItemClickListener)

    }

    override fun onBindViewHolder(holder: ThumbnailsViewHolder, position: Int) {
        val trailerModel = getItem(position)

        holder.bind(trailerModel)
        holder.onClickItem(trailerModel.videoKey)
    }

    class ThumbnailsViewHolder(private val binding: CellThumbnailBinding, private val context: Context,
                              private val onItemClickListener: ((videoKey: String) -> Unit)?) :
        RecyclerView.ViewHolder(binding.root) {

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

         fun onClickItem(videoId: String?) {
            binding.imageVideo.setOnClickListener {
                videoId?.let { onItemClickListener?.invoke(it) }
            }
        }

    }
}