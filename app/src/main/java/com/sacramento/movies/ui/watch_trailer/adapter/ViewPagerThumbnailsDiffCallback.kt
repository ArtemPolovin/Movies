package com.sacramento.movies.ui.watch_trailer.adapter

import androidx.recyclerview.widget.DiffUtil
import com.sacramento.domain.models.TrailerModel

class ViewPagerThumbnailsDiffCallback : DiffUtil.ItemCallback<TrailerModel>() {
    override fun areItemsTheSame(oldItem: TrailerModel, newItem: TrailerModel): Boolean {
       return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TrailerModel, newItem: TrailerModel): Boolean {
        return oldItem == newItem
    }

}