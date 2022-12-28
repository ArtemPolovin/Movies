package com.sacramento.movies.ui.saved_movie

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.sacramento.movies.R
import com.sacramento.movies.databinding.FragmentSavedMovieBinding
import com.sacramento.movies.ui.MainActivity
import com.sacramento.movies.ui.saved_movie.adapter.SavedMovieAdapter
import com.sacramento.movies.utils.MovieLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SavedMovieFragment : Fragment() {

    private var _binding: FragmentSavedMovieBinding? = null
    private val binding: FragmentSavedMovieBinding
        get() =
            _binding ?: throw RuntimeException("FragmentSavedMovieBinding =- null")

    private val viewModel: SavedMovieViewModel by viewModels()
    private val savedMoviesAdapter: SavedMovieAdapter by lazy { SavedMovieAdapter() }

    private var showDeleteMenuIcon = MutableLiveData<Boolean>()

    private var selectedMovieIdList = mutableListOf<Int>()

    private var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentSavedMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as MainActivity).setupActionBar(binding.toolbar)

        showDeleteMenuIcon.value = false

        setupAdapter()
        setupSavedMoviesList()
        openMovieDetailsScreen()
        receiveSelectedItemsFromAdapter()
        refreshSavedMoviesList()
    }

    private fun setupSavedMoviesList() {
        job?.cancel()
        job = null
        job = lifecycleScope.launch {
            viewModel.getSavedMovies().collectLatest {
                savedMoviesAdapter.submitData(it)
            }
        }
    }

    private fun setupAdapter() {
        binding.rvSavedMovies.apply {
            adapter = savedMoviesAdapter.withLoadStateHeaderAndFooter(
                header = MovieLoadStateAdapter { savedMoviesAdapter.retry() },
                footer = MovieLoadStateAdapter { savedMoviesAdapter.retry() }
            )
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
        savedMoviesAdapter.addLoadStateListener { loadState ->
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            binding.buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
            binding.textError.isVisible = loadState.source.refresh is LoadState.Error
        }
    }

    private fun openMovieDetailsScreen() {
        savedMoviesAdapter.onItemClickListener = { selectedMovie ->
            if (selectedMovie != null) {
                findNavController().navigate(
                    SavedMovieFragmentDirections.actionSavedMoviesToMovieDetailsFragment(
                        selectedMovie.movieId
                    )
                )
            }
        }
    }

    private fun receiveSelectedItemsFromAdapter() {
        savedMoviesAdapter.selectedElementsId.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                showDeleteMenuIcon.value = false
            } else {
                showDeleteMenuIcon.value = true
                selectedMovieIdList.clear()
                selectedMovieIdList.addAll(it)
            }
        }
    }

    private fun sendMovieIdList() {
        viewModel.deleteMoviesFromWatchList(selectedMovieIdList)
        savedMoviesAdapter.clearSelectedElementsList()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.saved_movies_toolbar_menu, menu)
        showDeleteMenuIcon.observe(viewLifecycleOwner, {
            val item: MenuItem = menu.findItem(R.id.delete_movie)
            item.isVisible = it
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_movie -> {
                sendMovieIdList()
                showDeleteMenuIcon.value = false
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun refreshSavedMoviesList() {
        viewModel.isSelectedMoviesDeleted.observe(viewLifecycleOwner) {
            if (it) {
                viewModel.clearLastFetchedMovieResult()
                setupSavedMoviesList()
            }
        }
    }
}