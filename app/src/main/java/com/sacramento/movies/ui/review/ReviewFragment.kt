package com.sacramento.movies.ui.review

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.sacramento.domain.models.ReviewModel
import com.sacramento.domain.models.TrailerModel
import com.sacramento.movies.R
import com.sacramento.movies.databinding.FragmentReviewBinding
import com.sacramento.movies.utils.BUNDLE_REVIEW_KEY
import com.sacramento.movies.utils.BUNDLE_TRAILER_LIST_KEY
import com.sacramento.movies.utils.getKSerializable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@AndroidEntryPoint
class ReviewFragment : Fragment() {

    private lateinit var binding: FragmentReviewBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupReview()
    }

    private fun setupReview() {
        val reviewModel = arguments?.getKSerializable<ReviewModel>(BUNDLE_REVIEW_KEY)
        binding.apply {
            loadRoundImage(reviewModel?.avatarPath ?: "", imageAvatar)
            textReviewerName.text = reviewModel?.userName
            textReviewDate.text = reviewModel?.createdAt
            textContent.text = reviewModel?.content
        }
    }

    private fun loadRoundImage(imageUrl: String, image: ImageView) {
        Glide.with(requireContext())
            .load(imageUrl)
            .error(R.drawable.ic_avatar)
            .into(image)
    }


}