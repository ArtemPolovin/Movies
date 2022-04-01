package com.example.movies.ui.move_details

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.models.MovieWithDetailsModel
import com.example.domain.models.SaveToWatchListModel
import com.example.domain.utils.ResponseResult
import com.example.movies.MovieDetailsArgs
import com.example.movies.R
import com.example.movies.databinding.FragmentMovieDetailsBinding
import com.example.movies.ui.move_details.adapter.MoviesAdapter
import com.example.movies.utils.MEDIA_TYPE_MOVIE
import com.example.movies.utils.bindingImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding: FragmentMovieDetailsBinding
        get() =
            _binding ?: throw RuntimeException("FragmentMovieDetailsBinding == null")

    private val viewModel: MovieDetailsViewModel by viewModels()

    private val args: MovieDetailsArgs by navArgs()

    private val movieId: Int by lazy {
        args.movieId
    }

    private lateinit var similarMoviesAdapter: MoviesAdapter
    private lateinit var recommendedMoviesAdapter: MoviesAdapter

    private var isSavedToWatchList = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_movie_details, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        checkMovieAccountState()
        checkMovieWithDetailsResponseState()
        openWebHomePage()
        setupRecyclerViewForSimilarMovies()
        setupRecyclerviewForRecommendedMovies()
        setupSimilarMoviesData()
        setupRecommendedMoviesData()
        openScreenWithMovieDetailsByClickingSimilarMovie()
        openScreenWithMovieDetailsByClickingRecommendedMovie()
        changeWatchListIconState()
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onResume() {
        super.onResume()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }


    private fun checkMovieWithDetailsResponseState() {

        viewModel.fetchMovieDetails(movieId)
        viewModel.movieDetailsModel.observe(viewLifecycleOwner) {
            binding.groupErrorViews.visibility = GONE
            when (it) {
                is ResponseResult.Loading -> {
                    binding.progressBar.visibility = VISIBLE
                }
                is ResponseResult.Failure -> {
                    binding.textError.visibility = VISIBLE
                    binding.textError.text = it.message
                    binding.buttonRetry.visibility = VISIBLE
                }
                is ResponseResult.Success -> {
                    setupMovieDetailsScreen(it.data)
                }
            }
        }
    }

    private fun setupMovieDetailsScreen(movieModel: MovieWithDetailsModel) {
        binding.groupErrorViews.visibility = GONE

        binding.movieWithDetailsModel = movieModel

        movieModel.homePageUrl?.let { underlineText(binding.textHomepageUrl, it) }
        movieModel.video?.let { watchTrailer(it) }

        showWatchTrailerButton(movieModel)
    }

    private fun showWatchTrailerButton(movieModel: MovieWithDetailsModel) {
        movieModel.video?.let { video ->
            if (video.isNotBlank()) binding.btnWatchTrailer.visibility = VISIBLE
            else binding.btnWatchTrailer.visibility = GONE
        }
    }

    private fun underlineText(textView: TextView, text: String) {
        val content = SpannableString(text)
        content.setSpan(UnderlineSpan(), 0, text.length, 0)
        textView.text = content
    }

    private fun openWebHomePage() {
        binding.textHomepageUrl.setOnClickListener {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(binding.textHomepageUrl.text.toString()))
            startActivity(browserIntent)
        }

    }

    private fun setupRecyclerViewForSimilarMovies() {
        similarMoviesAdapter = MoviesAdapter()
        binding.rvSimilarMovies.apply {
            adapter = similarMoviesAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupSimilarMoviesData() {
        viewModel.similarMovies.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseResult.Success -> {
                    similarMoviesAdapter.submitList(it.data)
                }
            }
        }
    }

    private fun openScreenWithMovieDetailsByClickingSimilarMovie() {
        similarMoviesAdapter.onItemClickListener = { movieId ->
            findNavController().navigate(
                MovieDetailsFragmentDirections.actionMovieDetailsFragmentSelf(movieId)
            )
        }
    }

    private fun setupRecyclerviewForRecommendedMovies() {
        recommendedMoviesAdapter = MoviesAdapter()
        binding.rvRecommendationsMovies.apply {
            adapter = recommendedMoviesAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupRecommendedMoviesData() {
        viewModel.recommendationsMovies.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseResult.Success -> {
                    recommendedMoviesAdapter.submitList(it.data)
                }
            }
        }
    }

    private fun openScreenWithMovieDetailsByClickingRecommendedMovie() {
        recommendedMoviesAdapter.onItemClickListener = { movieId ->
            findNavController().navigate(
                MovieDetailsFragmentDirections.actionMovieDetailsFragmentSelf(movieId)
            )
        }
    }

    private fun checkMovieAccountState() {
        viewModel.getMovieAccountState(movieId)
        viewModel.movieAccountState.observe(viewLifecycleOwner) { movieAccountState ->
            movieAccountState.watchlist?.let { it ->
                if (it) createSavedMovieIconStyle()
                else createDeletedMovieIconStyle()
                isSavedToWatchList = it
            }
        }
    }

    private fun changeWatchListIconState() {
        binding.imageViewSaveMovie.setOnClickListener {
            isSavedToWatchList = if (!isSavedToWatchList) {
                createSavedMovieIconStyle()
                true
            } else {
                createDeletedMovieIconStyle()
                false
            }
            viewModel.saveOrDeleteMovieFromWatchList(
                SaveToWatchListModel(
                    MEDIA_TYPE_MOVIE,
                    movieId,
                    isSavedToWatchList
                )
            )
        }
    }

    private fun createSavedMovieIconStyle() {
        val iconColor = Color.parseColor("#FFFFFF")
        binding.imageViewSaveMovie.setColorFilter(iconColor)
        binding.textSaveMovieIconText.apply {
            setTextColor(iconColor)
            text = getString(R.string.saved_in_watch_list)
        }
    }

    private fun createDeletedMovieIconStyle() {
        val iconColor = Color.parseColor("#918D8D")
        binding.imageViewSaveMovie.setColorFilter(iconColor)
        binding.textSaveMovieIconText.apply {
            setTextColor(iconColor)
            text = getString(R.string.save_to_watch_list)
        }
    }

    private fun watchTrailer(videoId: String) {
        binding.btnWatchTrailer.setOnClickListener {
            findNavController().navigate(
                MovieDetailsFragmentDirections.actionMovieDetailsFragmentToTrailer(
                    videoId
                )
            )
        }
    }

    override fun onStop() {
        super.onStop()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER
    }

}