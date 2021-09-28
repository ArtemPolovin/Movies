package com.example.movies.ui.movie_categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.data.utils.SharedPrefMovieCategory
import com.example.domain.utils.ResponseResult
import com.example.movies.R
import com.example.movies.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_movie_categories.*
import kotlinx.android.synthetic.main.fragment_movie_categories.button_retry
import kotlinx.android.synthetic.main.fragment_movie_categories.progress_bar
import kotlinx.android.synthetic.main.fragment_movie_categories.text_error
import kotlinx.android.synthetic.main.fragment_movie_categories.toolbar
import javax.inject.Inject

@AndroidEntryPoint
class MovieCategoriesFragment : Fragment() {

    private val viewModel: MovieCategoriesViewModel by viewModels()
    private lateinit var adapterMovieCategory: MoviesCategoriesAdapter

    @Inject
    lateinit var sharedPrefMovieCategory: SharedPrefMovieCategory

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
        viewModel.movieCategoriesCellsList.observe(viewLifecycleOwner){
            text_error.visibility = View.GONE
            button_retry.visibility = View.GONE
            progress_bar.visibility = View.GONE
            rv_movie_categories.visibility = View.GONE

            when (it) {
                is ResponseResult.Loading ->{
                    progress_bar.visibility = View.VISIBLE
                }
                is ResponseResult.Failure ->{
                    text_error.visibility = View.VISIBLE
                    text_error.text = it.message
                    button_retry.visibility = View.VISIBLE
                }
                is ResponseResult.Success ->{
                    rv_movie_categories.visibility = View.VISIBLE
                    adapterMovieCategory.setUpList(it.data)
                }
            }
        }
    }

    private fun openHomePage() {
        adapterMovieCategory.category.observe(viewLifecycleOwner){
            sharedPrefMovieCategory.saveMovieCategory(it.category)
            sharedPrefMovieCategory.saveGenreId(it.genreId)
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