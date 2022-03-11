package com.example.movies.ui.search_movie_by_name

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies.databinding.FragmentSearchMovieByNameBinding
import com.example.movies.utils.MovieLoadStateAdapter
import com.example.movies.ui.search_movie_by_name.adapters.MoviesAdapter
import com.example.movies.utils.KYE_MOVIE_NAME
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchMovieByNameFragment : Fragment() {

    private val viewModel: SearchMovieByNameViewModel by viewModels()

    private val movieAdapter: MoviesAdapter by lazy { MoviesAdapter() }

    private var _binding: FragmentSearchMovieByNameBinding? = null
    private val binding: FragmentSearchMovieByNameBinding
        get() = _binding ?: throw RuntimeException("FragmentSearchMovieByNameBinding is  null")

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
    }

    private fun setUpMovieList() {
        requireArguments().getString(KYE_MOVIE_NAME)?.let { movieName ->
            lifecycleScope.launch {
                viewModel.fetchMoviesByName(movieName).collectLatest { movieAdapter.submitData(it) }
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
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            binding.buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
            binding.textError.isVisible = loadState.source.refresh is LoadState.Error
        }
    }


    companion object {
        fun newInstanceSearchMovieByNameFragment(movieName: String): SearchMovieByNameFragment {
            return SearchMovieByNameFragment().apply {
                arguments = Bundle().apply {
                    putString(KYE_MOVIE_NAME, movieName)
                }
            }
        }
    }

}