package com.sacramento.movies.ui.review

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.sacramento.domain.models.ReviewModel
import com.sacramento.movies.MovieDetailsArgs
import com.sacramento.movies.R
import com.sacramento.movies.databinding.FragmentAllReviewsBinding
import com.sacramento.movies.ui.review.adapter.ReviewsAdapter
import com.sacramento.movies.utils.BUNDLE_REVIEWS_LIST_KEY
import com.sacramento.movies.utils.BUNDLE_REVIEW_KEY
import com.sacramento.movies.utils.MovieLoadStateAdapter
import com.sacramento.movies.utils.getKSerializable
import com.sacramento.movies.utils.putKSerializable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@AndroidEntryPoint
class AllReviewsFragment : Fragment() {

    private lateinit var binding: FragmentAllReviewsBinding
    private lateinit var reviewsAdapter: ReviewsAdapter

    private val viewModel: AllReviewsViewModel by viewModels()

    private val args: AllReviewsFragmentArgs by navArgs()

    private val movieId: Int by lazy {
        args.movieId
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAllReviewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupReviews()
        openReview()
    }

    private fun setupReviews() {
        lifecycleScope.launch {
            viewModel.getReviewsFromServer(movieId).collectLatest {
                reviewsAdapter.submitData(it)
            }
        }
    }

    private fun setupAdapter() {
        reviewsAdapter = ReviewsAdapter()
        binding.rvReviews.run {
            adapter = reviewsAdapter.withLoadStateHeaderAndFooter(
                header = MovieLoadStateAdapter { reviewsAdapter.retry() },
                footer = MovieLoadStateAdapter { reviewsAdapter.retry() }
            )
            layoutManager = LinearLayoutManager(requireContext())
        }

        reviewsAdapter.addLoadStateListener {loadState->
            binding.rvReviews.isVisible = loadState.source.refresh is LoadState.NotLoading
            binding.progressBarReviews.isVisible = loadState.source.refresh is LoadState.Error
            binding.groupErrorItems.isVisible = loadState.source.refresh is LoadState.Error
        }
    }

    private fun openReview() {
        reviewsAdapter.onItemClickListener = {reviewModel->
            val bundle = Bundle()
            bundle.putKSerializable(BUNDLE_REVIEW_KEY, reviewModel)
            findNavController().navigate(R.id.action_allReviewsFragment_to_reviewFragment, bundle)
        }
    }
}