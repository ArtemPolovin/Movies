package com.sacramento.movies.ui.move_details

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.math.MathUtils
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.sacramento.domain.models.MovieWithDetailsModel
import com.sacramento.domain.models.SaveToWatchListModel
import com.sacramento.domain.models.TrailerModel
import com.sacramento.domain.utils.ResponseResult
import com.sacramento.movies.MovieDetailsArgs
import com.sacramento.movies.R
import com.sacramento.movies.databinding.FragmentMovieDetailsBinding
import com.sacramento.movies.ui.move_details.adapter.MoviesAdapter
import com.sacramento.movies.utils.BUNDLE_TRAILER_LIST_KEY
import com.sacramento.movies.utils.KEY_MOVIE_ID
import com.sacramento.movies.utils.MEDIA_TYPE_MOVIE
import com.sacramento.movies.utils.putKSerializable
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.ExperimentalSerializationApi
import kotlin.math.abs

@ExperimentalSerializationApi
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

    companion object{
        fun newInstance(movieId: Int?): MovieDetailsFragment{
            return MovieDetailsFragment().apply {
                arguments = bundleOf(KEY_MOVIE_ID to movieId)
            }
        }
    }

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
        changePosterAlphaWhenScrolling()

       // changeStartDestinationIfAppOpenedByNotification()
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
        getTrailerList(movieModel.id)
    }

    private fun getTrailerList(movieId: Int) {
        viewModel.fetchTrailersFromNetwork(movieId)
        viewModel.trailerList.observe(viewLifecycleOwner) {

            when (it) {
                is ResponseResult.Success -> {
                    showWatchTrailerButton(it.data)
                    openScreenWithTrailer(it.data)
                }
                is ResponseResult.Failure -> {
                    binding.btnWatchTrailer.visibility = GONE
                }
                else -> binding.btnWatchTrailer.visibility = GONE
            }
        }
    }

    private fun showWatchTrailerButton(trailerList: List<TrailerModel>) {
        binding.btnWatchTrailer.visibility = if (trailerList.isNotEmpty()) VISIBLE else GONE
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
                }else -> null
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
                }else -> null
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
        viewModel.movieAccountState.observe(viewLifecycleOwner){
            when (it) {
               is ResponseResult.Failure ->{
                   Log.i("TAG", it.message)
                   findNavController().navigate(R.id.authorizationFragment)
               }
                is ResponseResult.Success ->{
                    it.data.watchlist?.let { isNotSavedToWatchList ->
                        if (isNotSavedToWatchList) createSavedMovieIconStyle()
                        else createDeletedMovieIconStyle()
                        isSavedToWatchList = isNotSavedToWatchList
                    }
                }else -> null
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
            viewModel.saveWatchListChangesState()
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

    private fun openScreenWithTrailer(trailerList: List<TrailerModel>) {
        binding.btnWatchTrailer.setOnClickListener {

            val bundle = Bundle()
            bundle.putKSerializable(BUNDLE_TRAILER_LIST_KEY, trailerList)

            findNavController().navigate(R.id.action_movieDetailsFragment_to_trailer, bundle)
        }
    }

    private fun changePosterAlphaWhenScrolling() {
        binding.myAppBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val offsetAlpha = abs(appBarLayout.y / binding.myAppBarLayout.totalScrollRange)
            val value = (1 - offsetAlpha)
            binding.imageMovieDetails.alpha = MathUtils.clamp(value, 0.25f, 1.0f)
        })
    }

    override fun onStop() {
        super.onStop()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER
    }

}
