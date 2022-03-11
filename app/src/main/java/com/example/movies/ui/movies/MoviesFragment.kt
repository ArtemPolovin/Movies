package com.example.movies.ui.movies

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.data.cache.SharedPrefMovieCategory
import com.example.domain.models.MovieWithDetailsModel
import com.example.movies.R
import com.example.movies.databinding.FragmentMoviesBinding
import com.example.movies.ui.MainActivity
import com.example.movies.ui.movies.adapter.MoviesWithDetailsAdapter
import com.example.movies.utils.MovieLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.RuntimeException
import javax.inject.Inject

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private var _binding: FragmentMoviesBinding? = null
    private val binding: FragmentMoviesBinding get() =
        _binding ?: throw RuntimeException("FragmentMoviesBinding = null")

    private val viewModel: MoviesViewModel by viewModels()

    private lateinit var moviesAdapter: MoviesWithDetailsAdapter

    @Inject
    lateinit var sharedPrefMovieCategory: SharedPrefMovieCategory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        (requireActivity() as MainActivity).setupActionBar(binding.toolbar)

        binding.buttonRetry.setOnClickListener { moviesAdapter.retry() }

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
        moviesAdapter = MoviesWithDetailsAdapter()
        binding.rvMovies.run {
            adapter = moviesAdapter.withLoadStateHeaderAndFooter(
                header = MovieLoadStateAdapter { moviesAdapter.retry() },
                footer = MovieLoadStateAdapter { moviesAdapter.retry() }
            )
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        moviesAdapter.addLoadStateListener { loadState ->
            binding.rvMovies?.let { it.isVisible = loadState.source.refresh is LoadState.NotLoading }
            binding.progressBar?.let { it.isVisible = loadState.source.refresh is LoadState.Loading }
            binding.textError?.let { it.isVisible = loadState.source.refresh is LoadState.Error }
            binding.buttonRetry?.let { it.isVisible = loadState.source.refresh is LoadState.Error }
        }
    }

    private fun openMovieDetailsScreen() {
        moviesAdapter.onClickItem(object : MoviesWithDetailsAdapter.OnClickAdapterPopularMovieListener {
            override fun getMovie(movie: MovieWithDetailsModel) {
                findNavController().navigate(
                    MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment(movie.id,true))
            }

        })
    }

    private fun setupToolbar() {
        binding.toolbar.title = sharedPrefMovieCategory.loadMovieCategory()
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filter -> {
                findNavController().navigate(R.id.action_homeFragment_to_moviesFilterFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }


    }

}