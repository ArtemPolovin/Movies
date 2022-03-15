package com.example.movies.ui.movie_categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.data.cache.SharedPrefMovieCategory
import com.example.data.cache.SharedPrefMovieFilter
import com.example.data.cache.clearMovieFilterCache
import com.example.domain.utils.ResponseResult
import com.example.movies.R
import com.example.movies.databinding.FragmentMovieCategoriesBinding
import com.example.movies.ui.MainActivity
import com.example.movies.ui.movie_categories.adapters.MoviesCategoriesAdapter
import com.example.movies.utils.KEY_OPEN_MOVIES_PAGE
import com.example.movies.utils.MINIMUM_SYMBOLS
import com.example.movies.utils.setNavigationResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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
                    adapterMovieCategory.setUpList(it.data)
                }
            }
        }
    }

    private fun openMoviesPage() {
        adapterMovieCategory.movieCategory.observe(viewLifecycleOwner) {
            sharedPrefMovieCategory.saveMovieCategory(it.categoryName)
            sharedPrefMovieCategory.saveGenreId(it.genreId)
            sharedPrefMovieFilter.clearFilterCache()
            //findNavController().navigate(R.id.action_movieCategoriesFragment_to_moviesFragment)
            setNavigationResult(KEY_OPEN_MOVIES_PAGE, true)
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