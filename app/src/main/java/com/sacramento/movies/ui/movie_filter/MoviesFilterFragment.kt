package com.sacramento.movies.ui.movie_filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sacramento.data.cache.SharedPrefMovieCategory
import com.sacramento.data.cache.SharedPrefMovieFilter
import com.sacramento.data.utils.MovieFilterParams
import com.sacramento.data.utils.POPULARITY_DATA
import com.sacramento.data.utils.SORT_BY_POPULARITY
import com.sacramento.movies.databinding.FragmentMoviesFilterBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MoviesFilterFragment : Fragment() {

    private var _binding: FragmentMoviesFilterBinding? = null
    private val binding: FragmentMoviesFilterBinding
        get() =
            _binding ?: throw RuntimeException("FragmentMoviesFilterBinding == null")

    @Inject
    lateinit var sharedPrefMovieFilter: SharedPrefMovieFilter

    @Inject
    lateinit var sharedPrefMovieCategory: SharedPrefMovieCategory

    private val viewModel: MovieFilterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        loadFilterState()
        setupGenresSpinner()
        removeFocusFromEditText()
        openMoviesScreen()


    }

    private fun loadFilterState() {
        binding.checkboxRatings.isChecked = sharedPrefMovieFilter.loadRatingCheckboxState()
        binding.spinnerRatings.setSelection(sharedPrefMovieFilter.loadRatingPosition())

        binding.checkboxReleaseYear.isChecked = sharedPrefMovieFilter.loadReleaseYearCheckBoxState()
        binding.editTextReleaseYear.setText(sharedPrefMovieFilter.loadReleaseYear().toString())

        /*spinner_genres.setSelection(sharedPrefMovieFilter.loadGenreSpinnerPosition())*/
        binding.checkboxGenres.isChecked = sharedPrefMovieFilter.loadGenreCheckBoxState()

        binding.checkboxPopularity.isChecked =
            sharedPrefMovieFilter.loadSortByPopularityCheckBoxState()

    }

    private fun setupGenresSpinner() {
        viewModel.genreNames.observe(viewLifecycleOwner) { genresList ->
            val arrayAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                genresList
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            binding.spinnerGenres.adapter = arrayAdapter
            binding.spinnerGenres.setSelection(sharedPrefMovieFilter.loadGenreSpinnerPosition())
        }
        //spinner_genres.onItemSelectedListener
    }

    private fun removeFocusFromEditText() {
        binding.editTextReleaseYear.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.editTextReleaseYear.clearFocus()
            }
            return@setOnEditorActionListener false
        }
    }

    private fun openMoviesScreen() {
        binding.btnSubmit.setOnClickListener {
            val rating = checkIfRatingIsMarked()
            val releaseYear = checkIfReleaseYearIsMarked()
            val genrePair = checkIfGenreCheckBoxIsMarked()
            val sortedByPopularity = checkIfSortByPopularityIsMarked()
            val destination =
                MoviesFilterFragmentDirections.actionMoviesFilterFragmentToMoviesFragment(
                    MovieFilterParams(
                        movieCategory = genrePair?.first,
                        genreId = genrePair?.second,
                        releaseYear = releaseYear,
                        sortByPopularity = sortedByPopularity,
                        rating = rating
                    )
                )
            findNavController().navigate(destination)
        }
    }

    private fun checkIfRatingIsMarked(): Int? {
        if (binding.checkboxRatings.isChecked) {
            binding.spinnerRatings.apply {
                val rating = selectedItem.toString().toInt()
                viewModel.saveRatingSate(rating, selectedItemPosition)
                return rating
            }
        }
        viewModel.clearRatingCache()
        return null
    }

    private fun checkIfReleaseYearIsMarked(): String? {
        if (binding.checkboxReleaseYear.isChecked) {
            val releaseYear = binding.editTextReleaseYear.text.toString()
            viewModel.saveReleaseYearState(releaseYear)
            return releaseYear
        }
        viewModel.clearReleaseYar()
        return null
    }

    private fun checkIfGenreCheckBoxIsMarked(): Pair<String, String>? {
        if (binding.checkboxGenres.isChecked) {
            binding.spinnerGenres.apply {
                val genreName = selectedItem.toString()
                val genreId = viewModel.saveGenreState(genreName, selectedItemPosition)
                return Pair(genreName, genreId)
            }
        }
        viewModel.clearGenreCache()
        return null
    }

    private fun checkIfSortByPopularityIsMarked(): String? {
        if (binding.checkboxPopularity.isChecked) {
            viewModel.saveSortByPopularityState()
            return POPULARITY_DATA
        }
        viewModel.clearSortByPopularityState()
        return null
    }
}