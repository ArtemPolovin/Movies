package com.sacramento.movies.ui.movie_categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.sacramento.data.cache.SharedPrefMovieCategory
import com.sacramento.data.cache.SharedPrefMovieFilter
import com.sacramento.domain.utils.ResponseResult
import com.sacramento.movies.R
import com.sacramento.movies.databinding.FragmentMovieCategoriesBinding
import com.sacramento.movies.ui.explore.ExploreFragmentDirections
import com.sacramento.movies.ui.movie_categories.adapters.MoviesCategoriesAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MovieCategoriesFragment : Fragment() {

    private var _binding: FragmentMovieCategoriesBinding? = null
    private val binding: FragmentMovieCategoriesBinding
        get() =
            _binding ?: throw RuntimeException("FragmentMovieCategoriesBinding == null")

    private val viewModel: MovieCategoriesViewModel by viewModels()
    private lateinit var adapterMovieCategory: MoviesCategoriesAdapter

    @Inject
    lateinit var sharedPrefMovieCategory: SharedPrefMovieCategory

    @Inject
    lateinit var sharedPrefMovieFilter: SharedPrefMovieFilter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setHasOptionsMenu(true)
        adapterMovieCategory = MoviesCategoriesAdapter()

        _binding = FragmentMovieCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.buttonRetry.setOnClickListener {
            viewModel.refreshData()
        }

        //setupToolbar()
        setupRecyclerView()
        setUpMovieCategoriesList()
        openMoviesPage()
        // inputResultOfMovieSearching()


    }

    private fun setUpMovieCategoriesList() {
        viewModel.moviesCategoriesList.observe(viewLifecycleOwner) {
            binding.groupErrorViews.visibility = GONE
            binding.rvMovieCategories.visibility = GONE
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
                    binding.rvMovieCategories.visibility = VISIBLE
                    adapterMovieCategory.submitList(it.data)
                }
            }
        }
    }

    private fun openMoviesPage() {
        adapterMovieCategory.movieCategory.observe(viewLifecycleOwner) {
            sharedPrefMovieCategory.saveMovieCategory(it.categoryName)
            sharedPrefMovieCategory.saveGenreId(it.genreId)
            sharedPrefMovieFilter.clearFilterCache()
            val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(ExploreFragmentDirections.actionExploreFragmentToMoviesFragment())
            //setNavigationResult(KEY_OPEN_MOVIES_PAGE, true)
        }
    }

    private fun setupRecyclerView() {
        binding.rvMovieCategories.run {
            adapter = adapterMovieCategory
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    /* private fun setupToolbar() {
         (requireActivity() as MainActivity).setupActionBar(binding.toolbar)
     }*/

}