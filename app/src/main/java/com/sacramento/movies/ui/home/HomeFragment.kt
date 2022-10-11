package com.sacramento.movies.ui.home

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.sacramento.data.cache.SharedPrefMovieCategory
import com.sacramento.data.cache.SharedPrefMovieFilter
import com.sacramento.data.utils.MovieFilterParams
import com.sacramento.domain.utils.ResponseResult
import com.sacramento.movies.databinding.FragmentHomeBinding
import com.sacramento.movies.ui.home.adapters.HomePosterViewPagerAdapter
import com.sacramento.movies.ui.home.adapters.HomeVerticalAdapter
import com.sacramento.movies.utils.DepthPageTransformer
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() =
            _binding ?: throw RuntimeException("FragmentHomeBinding == null")

    private lateinit var verticalAdapter: HomeVerticalAdapter

    private val viewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var sharedPrefMovieFilter: SharedPrefMovieFilter

    private val sliderHandle: Handler by lazy { Handler() }
    private val sliderRun: Runnable by lazy {
        Runnable {
            binding.viewPager.currentItem += 1
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        showSystemUI()

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.buttonRetry.setOnClickListener { viewModel.refreshData() }

        setupRecyclerView()
        setupViewPager()
        setupSortedMoviesList()
        openScreenWithAllMoviesWithGenre()
        openScreenWithMovieDetails()
    }

    private fun setupRecyclerView() {

        verticalAdapter = HomeVerticalAdapter()
        binding.rvVertical.apply {
            adapter = verticalAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

    }


    private fun setupSortedMoviesList() {
        viewModel.sortedMoviesByGenreModel.observe(viewLifecycleOwner) {
            binding.rvVertical.visibility = GONE
            binding.groupHiddenItems.visibility = GONE

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
                    binding.rvVertical.visibility = VISIBLE
                    verticalAdapter.submitList(it.data)
                }
            }
        }
    }

    private fun showSystemUI() {
        activity?.window?.decorView?.let {
            WindowCompat.setDecorFitsSystemWindows(requireActivity().window, true)
            WindowInsetsControllerCompat(
                this.requireActivity().window,
                it
            ).show(WindowInsetsCompat.Type.systemBars())
        }
    }

    private fun openScreenWithAllMoviesWithGenre() {
        verticalAdapter.genreData.observe(viewLifecycleOwner) { genre ->
            if (genre != null) {
                sharedPrefMovieFilter.clearFilterCache()
                val destination =
                    HomeFragmentDirections.actionHomeFragmentToMoviesFragment(MovieFilterParams(movieCategory = genre.genreName, genreId = genre.genreId))
                findNavController().navigate(destination)
            }
        }
    }

    private fun openScreenWithMovieDetails() {
        verticalAdapter.clickedMovieId.observe(viewLifecycleOwner) { movieId ->
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToMovieDetailsFragment(movieId)
            )
        }
    }

    private fun setupViewPager() {

        binding.viewPager.setPageTransformer(DepthPageTransformer())

        viewModel.upcomingMovies.observe(viewLifecycleOwner) {
            val posterAdapter = HomePosterViewPagerAdapter(it.toMutableList(), binding.viewPager)
            binding.viewPager.apply {
                adapter = posterAdapter
                clipToPadding = false
                clipChildren = false
                // view_pager.offscreenPageLimit = 3
                getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        sliderHandle.removeCallbacks(sliderRun)
                        sliderHandle.postDelayed(sliderRun, 3000)
                    }
                })

            }

            posterAdapter.onPosterClickListener = { movieId ->
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToMovieDetailsFragment(movieId)
                )
            }

        }
    }

    override fun onPause() {
        sliderHandle.removeCallbacks(sliderRun)
        super.onPause()
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onResume() {
        super.onResume()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        sliderHandle.postDelayed(sliderRun, 3000)
    }

    override fun onStop() {
        super.onStop()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER
    }


}