package com.example.movies.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.models.PopularMovieModel
import com.example.domain.models.PopularMovieWithDetailsModel
import com.example.movies.R
import com.example.movies.utils.ViewState
import com.example.movies.utils.putKSerializable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var moviesAdapter: MoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        moviesAdapter = MoviesAdapter()
        rv_movies.run {
            adapter = moviesAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        viewModel.refreshMoviesList()
        setupMoviesList()
        refreshList()
        openMovieDetailsScreen()

    }

    private fun setupMoviesList() {

        viewModel.popularMovies.observe(viewLifecycleOwner, {
            text_error.visibility = View.GONE
            refresh_layout.isRefreshing = false
            rv_movies.visibility = View.GONE

            when (it) {
                ViewState.Loading -> {
                    refresh_layout.isRefreshing = true
                }
                is ViewState.Failure -> {
                    text_error.visibility = View.VISIBLE
                    text_error.text = it.message
                }
                is ViewState.Success -> {
                    rv_movies.visibility = View.VISIBLE
                    moviesAdapter.setupMoveList(it.data)
                }
            }
        })
    }

    private fun openMovieDetailsScreen() {
        moviesAdapter.onClickItem(object : MoviesAdapter.OnclickMovieItemListener {
            override fun getMovieModel(movieWithDetailsModel: PopularMovieWithDetailsModel) {
                val bundle = Bundle()
                bundle.putKSerializable("movieObject", movieWithDetailsModel)
                findNavController().navigate(R.id.action_homeFragment_to_movieDetailsFragment,bundle)
            }

        })
    }

    private fun refreshList() {
        refresh_layout.setOnRefreshListener {
            viewModel.refreshMoviesList()
        }
    }


}