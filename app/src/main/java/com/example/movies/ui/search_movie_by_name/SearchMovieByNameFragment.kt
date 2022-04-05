package com.example.movies.ui.search_movie_by_name

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies.R
import com.example.movies.databinding.FragmentSearchMovieByNameBinding
import com.example.movies.ui.explore.ExploreFragmentDirections
import com.example.movies.utils.MovieLoadStateAdapter
import com.example.movies.ui.search_movie_by_name.adapters.MoviesAdapter
import com.example.movies.utils.KYE_MOVIE_NAME
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchMovieByNameFragment : Fragment() {

    private val viewModel: SearchMovieByNameViewModel by viewModels()

    private val movieAdapter: MoviesAdapter by lazy { MoviesAdapter() }

    private var _binding: FragmentSearchMovieByNameBinding? = null
    private val binding: FragmentSearchMovieByNameBinding
        get() = _binding ?: throw RuntimeException("FragmentSearchMovieByNameBinding is  null")

    private val args: SearchMovieByNameFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchMovieByNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setUpAdapter()
        setUpMovieList()
        openMovieDetailsScreen()
    }

    private fun setUpMovieList() {
        lifecycleScope.launch {
            viewModel.movies?.collectLatest { movieAdapter.submitData(it) }
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
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
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

}