package com.example.movies.ui.movie_categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.data.cache.SharedPrefMovieCategory
import com.example.data.cache.SharedPrefMovieFilter
import com.example.data.cache.clearMovieFilterCache
import com.example.domain.utils.ResponseResult
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

        button_retry.setOnClickListener {
            viewModel.refreshData()
        }

        setupToolbar()
        setupRecyclerView()
        setUpMovieCategoriesList()
        openMoviesPage()
        
    }

    private fun setUpMovieCategoriesList() {
        viewModel.moviesCategoriesList.observe(viewLifecycleOwner){
            group_error_views.visibility = GONE
            rv_movie_categories.visibility = GONE
            when (it) {
                is ResponseResult.Loading ->{
                    progress_bar.visibility = VISIBLE
                }
                is ResponseResult.Failure ->{
                    text_error.visibility = VISIBLE
                    text_error.text = it.message
                    button_retry.visibility = VISIBLE
                }
                is ResponseResult.Success ->{
                    rv_movie_categories.visibility = VISIBLE
                    adapterMovieCategory.setUpList(it.data)
                }
            }
        }
    }

    private fun openMoviesPage() {
        adapterMovieCategory.movieCategory.observe(viewLifecycleOwner){
            sharedPrefMovieCategory.saveMovieCategory(it.categoryName)
            sharedPrefMovieCategory.saveGenreId(it.genreId)
            clearMovieFilterCache(sharedPrefMovieFilter)
            findNavController().navigate(R.id.action_movie_categories_to_moviesFragment)
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