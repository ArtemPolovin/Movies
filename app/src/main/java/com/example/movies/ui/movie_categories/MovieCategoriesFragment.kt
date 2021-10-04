package com.example.movies.ui.movie_categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.data.cache.SharedPrefMovieCategory
import com.example.data.cache.SharedPrefMovieFilter
import com.example.data.cache.clearMovieFilterCache
import com.example.movies.R
import com.example.movies.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_movie_categories.*
import kotlinx.android.synthetic.main.fragment_movie_categories.toolbar
import javax.inject.Inject

@AndroidEntryPoint
class MovieCategoriesFragment : Fragment() {

    private val viewModel: MovieCategoriesViewModel by viewModels()
    private lateinit var adapterMovieCategory: MoviesCategoriesAdapter

    @Inject
    lateinit var sharedPrefMovieCategory: SharedPrefMovieCategory
    @Inject
    lateinit var sharedPrefMovieFilter: SharedPrefMovieFilter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        adapterMovieCategory = MoviesCategoriesAdapter()

        return inflater.inflate(R.layout.fragment_movie_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupToolbar()
        setupRecyclerView()
        setUpMovieCategoriesList()
        openHomePage()
        
    }

    private fun setUpMovieCategoriesList() {
        adapterMovieCategory.setUpList(viewModel.fetchMovieCategoriesList())
    }

    private fun openHomePage() {
        adapterMovieCategory.category.observe(viewLifecycleOwner){
            sharedPrefMovieCategory.saveMovieCategory(it.categoryName)
            sharedPrefMovieCategory.saveGenreId(it.genreId)
            clearMovieFilterCache(sharedPrefMovieFilter)
            findNavController().navigate(R.id.action_movie_categories_to_homeFragment)
        }
    }

    private fun setupRecyclerView() {
        rv_movie_categories.run {
            adapter = adapterMovieCategory
            layoutManager = GridLayoutManager(requireContext(),2)
        }
    }

    private fun setupToolbar() {
        (requireActivity() as MainActivity).setupActionBar(toolbar)
    }
}