package com.example.movies.ui.explore

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.movies.R
import com.example.movies.databinding.FragmentExploreBinding
import com.example.movies.ui.MainActivity
import com.example.movies.ui.movie_categories.MovieCategoriesFragmentDirections
import com.example.movies.ui.search_movie_by_name.SearchMovieByNameFragmentDirections
import com.example.movies.utils.MINIMUM_SYMBOLS
import com.example.movies.utils.MOVIE_CATEGORIES_FRAGMENT
import com.example.movies.utils.SEARCH_MOVIE_BY_NAME_FRAGMENT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@FlowPreview
@AndroidEntryPoint
class ExploreFragment : Fragment() {

    private lateinit var navController: NavController

    private var prevSearchViewText = ""

    private var _binding: FragmentExploreBinding? = null
    private val binding: FragmentExploreBinding
        get() =
            _binding ?: throw RuntimeException("FragmentExploreBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_explore_fragment) as NavHostFragment

        navController = navHostFragment.navController

        setupToolbar()
        inputResultOfMovieSearching()
        setSearchViewAccordingCurrentFragment()
    }

    private fun switchFragment(movieName: String) {

        val destination =
            if (navController.currentDestination?.label == SEARCH_MOVIE_BY_NAME_FRAGMENT) {
                binding.exploreConstraintLayout.setBackgroundResource(R.color.home_page_background_color)
                SearchMovieByNameFragmentDirections.actionSearchMovieByNameFragmentSelf(movieName)
            } else {
                MovieCategoriesFragmentDirections.actionMovieCategoriesFragmentToSearchMovieByNameFragment(
                    movieName
                )
            }
        navController.navigate(destination)
    }

    private fun resultFromSearchView(): StateFlow<String> {
        val query = MutableStateFlow("")

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    if(it == prevSearchViewText) return@let
                    query.value = it
                    prevSearchViewText = it
                }
                return true
            }
        })
        return query
    }


    private fun inputResultOfMovieSearching() {
        viewLifecycleOwner.lifecycleScope.launch {
            resultFromSearchView()
                .debounce(500)
                .filter { it.length >= MINIMUM_SYMBOLS }
              //  .distinctUntilChanged()
                .flowOn(Dispatchers.Default)
                .collect { result ->
                    switchFragment(result)
                }
        }
    }

    private fun setupToolbar() {
        (requireActivity() as MainActivity).setupActionBar(binding.toolbar)
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.title =
            getString(R.string.explore_fragment_title)
    }

    private fun setSearchViewAccordingCurrentFragment() {
        navController.addOnDestinationChangedListener{controller, destination, arguments ->

            when (destination.label) {
                MOVIE_CATEGORIES_FRAGMENT ->{
                    binding.exploreConstraintLayout.setBackgroundResource(R.color.white)
                    binding.searchView.setQuery("",true)
                }
                SEARCH_MOVIE_BY_NAME_FRAGMENT ->{
                    binding.exploreConstraintLayout.setBackgroundResource(R.color.home_page_background_color)
                }
            }
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onResume() {
        super.onResume()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onStop() {
        super.onStop()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER
    }

}