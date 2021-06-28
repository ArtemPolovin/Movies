package com.example.movies.ui.saved_movie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.domain.models.PopularMovieWithDetailsModel
import com.example.domain.models.utils.ResponseResult
import com.example.movies.R
import com.example.movies.ui.MainActivity
import com.example.movies.utils.OnClickAdapterPopularMovieListener
import com.example.movies.utils.putKSerializable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_saved_movie.*
import kotlinx.android.synthetic.main.fragment_saved_movie.button_retry
import kotlinx.android.synthetic.main.fragment_saved_movie.progress_bar
import kotlinx.android.synthetic.main.fragment_saved_movie.text_error
import kotlinx.android.synthetic.main.fragment_saved_movie.toolbar

@AndroidEntryPoint
class SavedMovieFragment : Fragment() {

    private val viewModel: SavedMovieViewModel by viewModels()
    private lateinit var savedMoviesAdapter: SavedMovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        (requireActivity() as MainActivity).setupActionBar(toolbar)

        setupAdapter()
        setupSavedMoviesList()
        openMovieDetailsScreen()
    }

    private fun setupSavedMoviesList() {

        viewModel.savedMoviesList.observe(viewLifecycleOwner, {

            text_error.visibility = View.GONE
            progress_bar.visibility = View.GONE
            rv_saved_movies.visibility = View.GONE
            button_retry.visibility = View.GONE

            when (it) {
                ResponseResult.Loading ->{
                    progress_bar.visibility = View.VISIBLE
                }
               is ResponseResult.Failure -> {
                   button_retry.visibility = View.GONE
                   progress_bar.visibility = View.GONE
                   text_error.visibility = View.VISIBLE
                   text_error.text = it.message
               }
                is ResponseResult.Success ->{
                    progress_bar.visibility = View.GONE
                    rv_saved_movies.visibility = View.VISIBLE
                    savedMoviesAdapter.setupList(it.data)
                }
            }
        })



    }

    private fun setupAdapter() {
        savedMoviesAdapter = SavedMovieAdapter()
        rv_saved_movies.run {
            adapter = savedMoviesAdapter
            layoutManager = GridLayoutManager(requireContext(),2)
        }
    }


    private fun openMovieDetailsScreen() {
        savedMoviesAdapter.onclick(object : OnClickAdapterPopularMovieListener {
            override fun getPopularMovie(movie: PopularMovieWithDetailsModel) {
                val bundle = Bundle()
                bundle.putKSerializable("movieObject", movie)
                bundle.putBoolean("showSavedIcon", false)
                findNavController().navigate(
                    R.id.action_saved_movies_to_movieDetailsFragment,
                    bundle
                )
            }
        })

    }
}