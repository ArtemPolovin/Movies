package com.example.movies.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.models.PopularMovieWithDetailsModel
import com.example.movies.R
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        button_retry.setOnClickListener { moviesAdapter.retry() }

        setupRecyclerView()
        setupLoadState()
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

    private fun setupLoadState() {
        lifecycleScope.launch {
            moviesAdapter.loadStateFlow.collectLatest { loadState ->
                progress_bar.isVisible = loadState.refresh is LoadState.Loading
                button_retry.isVisible = loadState.refresh is LoadState.Error
                text_error.isVisible = loadState.refresh is LoadState.Error
            }
        }
    }

    private fun setupRecyclerView() {
        moviesAdapter = MovieAdapter()
        rv_movies.run {
            adapter = moviesAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
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

}