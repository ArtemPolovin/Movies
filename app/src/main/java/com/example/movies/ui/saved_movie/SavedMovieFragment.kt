package com.example.movies.ui.saved_movie

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.domain.utils.ResponseResult
import com.example.movies.R
import com.example.movies.databinding.FragmentSavedMovieBinding
import com.example.movies.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.RuntimeException

@AndroidEntryPoint
class SavedMovieFragment : Fragment() {

    private var _binding: FragmentSavedMovieBinding? = null
    private val binding: FragmentSavedMovieBinding get() =
        _binding ?: throw RuntimeException("FragmentSavedMovieBinding =- null")

    private val viewModel: SavedMovieViewModel by viewModels()
    private lateinit var savedMoviesAdapter: SavedMovieAdapter

    private var showDeleteMenuIcon = MutableLiveData<Boolean>()

    private var popularMovieId = mutableListOf<Int>()

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
    }

    private fun setupSavedMoviesList() {

        viewModel.savedMovie.observe(viewLifecycleOwner, {

            binding.textError.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
            binding.rvSavedMovies.visibility = View.GONE
            binding.buttonRetry.visibility = View.GONE

            when (it) {
                ResponseResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is ResponseResult.Failure -> {
                    binding.buttonRetry.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    binding.textError.visibility = View.VISIBLE
                    binding.textError.text = it.message
                }
                is ResponseResult.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.rvSavedMovies.visibility = View.VISIBLE
                    savedMoviesAdapter.setupList(it.data)
                }
            }
        })


    }

    private fun setupAdapter() {
        savedMoviesAdapter = SavedMovieAdapter()
        binding.rvSavedMovies.run {
            adapter = savedMoviesAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }


    private fun openMovieDetailsScreen() {
        savedMoviesAdapter.selectedMovie.observe(viewLifecycleOwner) { selectedMovie ->
            findNavController().navigate(
                SavedMovieFragmentDirections.actionSavedMoviesToMovieDetailsFragment(
                    selectedMovie.id,
                    false
                )
            )
        }
    }

    private fun receiveSelectedItemsFromAdapter() {
        savedMoviesAdapter.selectedElementsId.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                showDeleteMenuIcon.value = false
            } else {
                showDeleteMenuIcon.value = true
                popularMovieId.clear()
                popularMovieId.addAll(it)
            }
        }
    }

    private fun deleteMovieFromList() {
        lifecycleScope.launch {
            viewModel.deleteMovieByIdFromDb(popularMovieId)
            savedMoviesAdapter.clearSelectedElementsList()
        }
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
                deleteMovieFromList()
                showDeleteMenuIcon.value = false
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }
}