package com.example.movies.ui.movie_filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.data.cache.SharedPrefMovieCategory
import com.example.data.cache.SharedPrefMovieFilter
import com.example.movies.R
import com.example.movies.databinding.FragmentMoviesFilterBinding
import dagger.hilt.android.AndroidEntryPoint
import java.lang.RuntimeException
import javax.inject.Inject

@AndroidEntryPoint
class MoviesFilterFragment : Fragment(){

    private var _binding: FragmentMoviesFilterBinding? = null
    private val binding: FragmentMoviesFilterBinding get() =
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
        openHomePage()


    }

    private fun loadFilterState() {
        binding.checkboxRatings.isChecked = sharedPrefMovieFilter.loadRatingCheckboxState()
        binding.spinnerRatings.setSelection(sharedPrefMovieFilter.loadRatingPosition())

        binding.checkboxReleaseYear.isChecked = sharedPrefMovieFilter.loadReleaseYearCheckBoxState()
        binding.editTextReleaseYear.setText(sharedPrefMovieFilter.loadReleaseYear().toString())

        /*spinner_genres.setSelection(sharedPrefMovieFilter.loadGenreSpinnerPosition())*/
        binding.checkboxGenres.isChecked = sharedPrefMovieFilter.loadGenreCheckBoxState()

        binding.checkboxPopularity.isChecked = sharedPrefMovieFilter.loadSortByPopularityCheckBoxState()

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

    private fun openHomePage() {
        binding.btnSubmit.setOnClickListener {
            checkIfRatingIsMarked()
            checkIfReleaseYearIsMarked()
            checkIfGenreCheckBoxIsMarked()
            checkIfSortByPopularityIsMarked()
            findNavController().navigate(R.id.action_moviesFilterFragment_to_homeFragment)
        }
    }

    private fun checkIfRatingIsMarked() {
        if (binding.checkboxRatings.isChecked) {
            binding.spinnerRatings.apply {
                viewModel.saveRatingSate(selectedItem.toString().toInt(), selectedItemPosition)
            }
            return
        }
        viewModel.clearRatingCache()
    }

    private fun checkIfReleaseYearIsMarked() {
        if (binding.checkboxReleaseYear.isChecked) {
            viewModel.saveReleaseYearState(binding.editTextReleaseYear.text.toString())
            return
        }
        viewModel.clearReleaseYar()
    }

    private fun checkIfGenreCheckBoxIsMarked() {
        if (binding.checkboxGenres.isChecked) {
            binding.spinnerGenres.apply {
                viewModel.saveGenreState(selectedItem.toString(), selectedItemPosition)
            }
            return
        }
        viewModel.clearGenreCache()
    }

    private fun checkIfSortByPopularityIsMarked() {
        if (binding.checkboxPopularity.isChecked) {
            viewModel.saveSortByPopularityState()
            return
        }
        viewModel.clearSortByPopularityState()
    }
}