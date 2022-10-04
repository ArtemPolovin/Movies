package com.sacramento.movies.ui.search_movie_by_name

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.sacramento.movies.R
import com.sacramento.movies.databinding.FragmentSearchMovieByNameBinding
import com.sacramento.movies.ui.explore.ExploreFragmentDirections
import com.sacramento.movies.utils.MovieLoadStateAdapter
import com.sacramento.movies.ui.search_movie_by_name.adapters.MoviesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchMovieByNameFragment : Fragment() {

    private val viewModel: SearchMovieByNameViewModel by viewModels()

    private val movieAdapter: MoviesAdapter by lazy { MoviesAdapter() }

    private var _binding: FragmentSearchMovieByNameBinding? = null
    private val binding: FragmentSearchMovieByNameBinding
        get() = _binding ?: throw RuntimeException("FragmentSearchMovieByNameBinding is  null")

    private var job: Job? = null

    private val args: SearchMovieByNameFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchMovieByNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        refreshList()
        setUpAdapter()
        setUpMovieList()
        openMovieDetailsScreen()
    }

    private fun setUpMovieList() {
       job =  lifecycleScope.launch {
            viewModel.getMovies().collectLatest {
                movieAdapter.submitData(it)
            }
        }
    }

    private fun setUpAdapter() {
        binding.rvExploreMovies.apply {
            adapter = movieAdapter.withLoadStateHeaderAndFooter(
                header = MovieLoadStateAdapter { movieAdapter.retry() },
                footer = MovieLoadStateAdapter { movieAdapter.retry() }
            )
            layoutManager = GridLayoutManager(requireContext(), 3)
        }

        movieAdapter.addLoadStateListener { loadState ->
            binding.rvExploreMovies.isVisible = loadState.source.refresh is LoadState.NotLoading
            binding.swipeRefresh.isRefreshing = loadState.source.refresh is LoadState.Loading
            binding.buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
            binding.textError.isVisible = loadState.source.refresh is LoadState.Error
        }
    }

    private fun openMovieDetailsScreen() {
        movieAdapter.onItemClickLister = {movieId ->
            val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(ExploreFragmentDirections.actionExploreFragmentToMovieDetails(movieId))
        }
    }

    private fun refreshList() {
        binding.swipeRefresh.setOnRefreshListener {
            job?.cancel()
            job = null
            viewModel.refreshList()
            setUpMovieList()
        }
    }

}