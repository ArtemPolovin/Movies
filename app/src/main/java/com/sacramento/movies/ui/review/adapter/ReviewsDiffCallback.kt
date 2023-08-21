package com.sacramento.movies.ui.review.adapter

import androidx.recyclerview.widget.DiffUtil
import com.sacramento.domain.models.ReviewModel

class ReviewsDiffCallback : DiffUtil.ItemCallback<ReviewModel>() {
    override fun areItemsTheSame(oldItem: ReviewModel, newItem: ReviewModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ReviewModel, newItem: ReviewModel): Boolean {
        return oldItem == newItem
    }

}