package com.sacramento.movies.ui.review.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sacramento.domain.models.ReviewModel
import com.sacramento.movies.R
import com.sacramento.movies.databinding.CellMovieReviewBinding

class ReviewsAdapter :
    PagingDataAdapter<ReviewModel, ReviewsAdapter.ReviewsViewHolder>(ReviewsDiffCallback()) {

    var onItemClickListener: ((reviewModel: ReviewModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsViewHolder {
        val binding = CellMovieReviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ReviewsViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ReviewsViewHolder, position: Int) {
        getItem(position)?.let { reviewModel ->
            holder.onItemClick(reviewModel)
            holder.bind(reviewModel)
        }
    }

    inner class ReviewsViewHolder(
        private val binding: CellMovieReviewBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(reviewModel: ReviewModel) {
            binding.apply {
                loadRoundImage(reviewModel.avatarPath ?: "", imageAvatar)
                txtUserName.text = reviewModel.userName
                txtRating.text = reviewModel.rating
                txtWrittenDate.text = "Written by ${reviewModel.userName} on ${reviewModel.createdAt}"
                txtContent.text = reviewModel.content
            }
        }

        private fun loadRoundImage(imageUrl: String, image: ImageView) {
            Glide.with(context)
                .load(imageUrl)
                .error(R.drawable.ic_avatar)
                .into(image)
        }

        fun onItemClick(reviewModel: ReviewModel) {
            itemView.setOnClickListener {
                onItemClickListener?.invoke(reviewModel)
            }
        }
    }
}