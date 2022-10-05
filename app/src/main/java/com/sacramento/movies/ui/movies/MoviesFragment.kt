package com.sacramento.movies.ui.movies

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
import com.sacramento.data.cache.SharedPrefMovieCategory
import com.sacramento.domain.models.MovieWithDetailsModel
import com.sacramento.movies.R
import com.sacramento.movies.databinding.FragmentMoviesBinding
import com.sacramento.movies.ui.MainActivity
import com.sacramento.movies.ui.movies.adapter.MoviesWithDetailsAdapter
import com.sacramento.movies.utils.MovieLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private var _binding: FragmentMoviesBinding? = null
    private val binding: FragmentMoviesBinding
        get() =
            _binding ?: throw RuntimeException("FragmentMoviesBinding = null")

    private var job: Job? = null

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

        refreshList()
        setupToolbar()
        setupAdapter()
        setupMoviesList()
        openMovieDetailsScreen()


    }

    private fun setupMoviesList() {
        job?.cancel()
        job = null
        job = lifecycleScope.launch {
            viewModel.getMovies().collectLatest {
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
            binding.rvMovies.isVisible = loadState.source.refresh is LoadState.NotLoading
            binding.pullRefreshLayout.isRefreshing = loadState.source.refresh is LoadState.Loading
            binding.textError.isVisible = loadState.source.refresh is LoadState.Error
            binding.buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
        }
    }

    private fun openMovieDetailsScreen() {
        moviesAdapter.onClickItem(object :
            MoviesWithDetailsAdapter.OnClickAdapterPopularMovieListener {
            override fun getMovie(movie: MovieWithDetailsModel) {
                findNavController().navigate(
                    MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment(movie.id)
                )
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

    private fun refreshList() {
        binding.pullRefreshLayout.setOnRefreshListener {
            viewModel.clearLastFetchedData()
            setupMoviesList()
        }
    }


}