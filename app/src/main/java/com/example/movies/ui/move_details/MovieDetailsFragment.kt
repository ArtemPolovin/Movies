package com.example.movies.ui.move_details

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.*
import android.view.View.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.domain.models.MovieWithDetailsModel
import com.example.domain.utils.ResponseResult
import com.example.movies.R
import com.example.movies.databinding.FragmentMovieDetailsBinding
import com.example.movies.ui.MainActivity
import com.example.movies.ui.move_details.adapter.MoviesAdapter
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import dagger.hilt.android.AndroidEntryPoint
import java.lang.RuntimeException

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding: FragmentMovieDetailsBinding get() =
        _binding ?: throw RuntimeException("FragmentMovieDetailsBinding == null")

    private val viewModel: MovieDetailsViewModel by viewModels()

    private val args: MovieDetailsFragmentArgs by navArgs()

    private lateinit var similarMoviesAdapter: MoviesAdapter
    private lateinit var recommendedMoviesAdapter: MoviesAdapter

    private var movieWithDetails: MovieWithDetailsModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

       _binding = FragmentMovieDetailsBinding.inflate(inflater,container, false)
        return binding.root
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        (requireActivity() as MainActivity).setupActionBar(binding.toolbar, false)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


        checkMovieWithDetailsResponseState()
        openWebHomePage()
        fullScreenListener()
        setupRecyclerViewForSimilarMovies()
        setupRecyclerviewForRecommendedMovies()
        setupSimilarMoviesData()
        setupRecommendedMoviesData()
        openScreenWithMovieDetailsByClickingSimilarMovie()
        openScreenWithMovieDetailsByClickingRecommendedMovie()
    }


    private fun checkMovieWithDetailsResponseState() {

        viewModel.fetchMovieDetails(args.movieId)
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
                    setupMovieDetails(it.data)
                }
            }
        }

    }

    private fun setupMovieDetails(movieModel: MovieWithDetailsModel) {
        binding.groupErrorViews.visibility = GONE
        binding.youtubePlayer.visibility = GONE
        binding.imageMovieDetails.visibility = GONE

        movieModel.apply {
            binding.textMovieNameDetails.text = movieName
            binding.textPopularity.text = popularityScore
            binding.textReleaseDate.text = releaseData
            binding.textOverview.text = overview
            binding.textVoteCountDetails.text = "(${voteCount})"
            rating?.let { binding.ratingBarMovie.rating = it.toFloat() }
            binding.textRatingDetails.text = rating.toString()
            binding.textGenre.text = genres
            homePageUrl?.let { underlineText(binding.textHomepageUrl, it) }
            showVideoOrPoster(movieModel)
        }
    }

    private fun showVideoOrPoster(movieModel: MovieWithDetailsModel) {
        movieModel.video?.let { video ->
            if (video.isNotBlank()) {
                binding.imageMovieDetails.visibility = GONE
                binding.viewImageShadow.visibility = GONE
                binding.youtubePlayer.visibility = VISIBLE
                showVideo(video)
            } else {
                binding.imageMovieDetails.visibility = VISIBLE
                binding.viewImageShadow.visibility = VISIBLE

                Glide.with(requireActivity())
                    .load(movieModel.backdropPoster)
                    .into(binding.imageMovieDetails)
            }
        }

    }

    private fun showVideo(videoId: String) {
        viewLifecycleOwner.lifecycle.addObserver(binding.youtubePlayer)

        binding.youtubePlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.cueVideo(videoId, 0f)
            }
        })
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.details_screen_tool_bar_menu, menu)
        val item: MenuItem = menu.findItem(R.id.save_movie)
        item.isVisible = args.showSavedIcon
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_movie -> {
                movieWithDetails?.let { viewModel.insertMovieToDb(it) }
                Toast.makeText(requireContext(), "The movie was saved", Toast.LENGTH_LONG).show()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun fullScreenListener() {

        val decorView = activity?.window?.decorView?.let {
            val screenListener = object : YouTubePlayerFullScreenListener {
                override fun onYouTubePlayerEnterFullScreen() {
                    binding.youtubePlayer.enterFullScreen()
                    hideSystemUi(it)
                }

                override fun onYouTubePlayerExitFullScreen() {
                    showSystemUi(it)
                }

            }
            binding.youtubePlayer.addFullScreenListener(screenListener)
        }

    }

    private fun hideSystemUi(view: View) {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        MainActivity.hideBottomNavBar()
        binding.toolbar.visibility = GONE

        view.systemUiVisibility =
            SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    SYSTEM_UI_FLAG_FULLSCREEN or
                    SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun showSystemUi(view: View) {

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        MainActivity.showBottomNavBar()

        view.systemUiVisibility =
            SYSTEM_UI_FLAG_LAYOUT_STABLE or SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        binding.toolbar.visibility = VISIBLE
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
                MovieDetailsFragmentDirections.actionMovieDetailsFragmentSelf(
                    movieId,true
                )
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
                MovieDetailsFragmentDirections.actionMovieDetailsFragmentSelf(
                    movieId, true
                )
            )
        }
    }

    override fun onStop() {
        super.onStop()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER
    }

}