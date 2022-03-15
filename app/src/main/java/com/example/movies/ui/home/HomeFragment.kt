package com.example.movies.ui.home

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.data.cache.SharedPrefMovieCategory
import com.example.data.cache.SharedPrefMovieFilter
import com.example.domain.utils.ResponseResult
import com.example.movies.R
import com.example.movies.databinding.FragmentHomeBinding
import com.example.movies.ui.home.adapters.HomePosterViewPagerAdapter
import com.example.movies.ui.home.adapters.HomeVerticalAdapter
import com.example.movies.utils.DepthPageTransformer
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
    lateinit var sharedPrefMovieCategory: SharedPrefMovieCategory

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

        //return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.buttonRetry.setOnClickListener { viewModel.refreshData() }


        //app_bar_layout.addOnOffsetChangedListener(ifAppbarLayoutIsCollapsed)

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

            //layoutManager = mLayoutManager
        }

        //val firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition()

        //println("mLog: first pos = ${(rv_vertical.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()}")
        //println("mLog: first pos = $firstVisibleItemPosition")
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
                    verticalAdapter.setData(it.data)
                }
            }
        }
    }

    private fun showSystemUI() {
        activity?.window?.decorView?.let {
            it.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
    }

    private fun openScreenWithAllMoviesWithGenre() {
        verticalAdapter.genreData.observe(viewLifecycleOwner) { genre ->
            if (genre != null) {
                sharedPrefMovieCategory.saveMovieCategory(genre.genreName)
                sharedPrefMovieCategory.saveGenreId(genre.genreId)
                sharedPrefMovieFilter.clearFilterCache()
                findNavController().navigate(R.id.action_homeFragment_to_moviesFragment)
            }
        }
    }

    private fun openScreenWithMovieDetails() {
        verticalAdapter.clickedMovieId.observe(viewLifecycleOwner) { movieId ->
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToMovieDetailsFragment(movieId, true)
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
                    HomeFragmentDirections.actionHomeFragmentToMovieDetailsFragment(movieId, true)
                )
            }

        }
    }

    override fun onPause() {
        sliderHandle.removeCallbacks(sliderRun)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        sliderHandle.postDelayed(sliderRun, 3000)
    }

}