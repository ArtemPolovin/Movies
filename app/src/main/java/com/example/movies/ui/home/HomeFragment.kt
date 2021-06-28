package com.example.movies.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.models.PopularMovieWithDetailsModel
import com.example.movies.R
import com.example.movies.ui.MainActivity
import com.example.movies.ui.home.adapter.MovieAdapter
import com.example.movies.ui.home.adapter.MovieLoadStateAdapter
import com.example.movies.utils.putKSerializable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var moviesAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        (requireActivity() as MainActivity).setupActionBar(toolbar)

        button_retry.setOnClickListener { moviesAdapter.retry() }

        setupToolbar()
        setupAdapter()
        setupMoviesList()
        openMovieDetailsScreen()

    }

    private fun setupMoviesList() {

        lifecycleScope.launch {
            viewModel.fetchPopularMoviesWithDetails().collectLatest {
                moviesAdapter.submitData(it)
            }
        }
    }


    private fun setupAdapter() {
        moviesAdapter = MovieAdapter()
        rv_movies.run {
            adapter = moviesAdapter.withLoadStateHeaderAndFooter(
                header = MovieLoadStateAdapter{moviesAdapter.retry()},
                footer = MovieLoadStateAdapter{moviesAdapter.retry()}
            )
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        moviesAdapter.addLoadStateListener { loadState ->
            rv_movies?.let{it.isVisible = loadState.source.refresh is LoadState.NotLoading}
            progress_bar?.let{it.isVisible = loadState.source.refresh is LoadState.Loading}
            text_error?.let{it.isVisible = loadState.source.refresh is LoadState.Error}
            button_retry?.let{it.isVisible = loadState.source.refresh is LoadState.Error}
        }
    }

    private fun openMovieDetailsScreen() {
        moviesAdapter.onClickItem(object : MovieAdapter.OnclickMovieItemListener {
            override fun getMovieModel(movieWithDetailsModel: PopularMovieWithDetailsModel) {
                val bundle = Bundle()
                bundle.putKSerializable("movieObject", movieWithDetailsModel)
                findNavController().navigate(
                    R.id.action_homeFragment_to_movieDetailsFragment,
                    bundle
                )
            }

        })
    }

    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)

    }

}