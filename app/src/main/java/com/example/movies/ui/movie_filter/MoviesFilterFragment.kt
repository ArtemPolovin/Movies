package com.example.movies.ui.movie_filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.data.cache.SharedPrefMovieCategory
import com.example.data.cache.SharedPrefMovieFilter
import com.example.movies.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_movies_filter.*
import javax.inject.Inject

@AndroidEntryPoint
class MoviesFilterFragment : Fragment() {

    @Inject
    lateinit var sharedPrefMovieFilter: SharedPrefMovieFilter

    @Inject
    lateinit var sharedPrefMovieCategory: SharedPrefMovieCategory

    private val viewModel: MovieFilterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        loadFilterState()
        removeFocusFromEditText()
        openHomePage()


    }

    private fun loadFilterState() {
        checkbox_ratings.isChecked = sharedPrefMovieFilter.loadRatingCheckboxState()
        spinner_ratings.setSelection(sharedPrefMovieFilter.loadRatingPosition())

        checkbox_release_year.isChecked = sharedPrefMovieFilter.loadReleaseYearCheckBoxState()
        edit_text_release_year.setText(sharedPrefMovieFilter.loadReleaseYear().toString())

        spinner_genres.setSelection(sharedPrefMovieFilter.loadGenreSpinnerPosition())
        checkbox_genres.isChecked = sharedPrefMovieFilter.loadGenreCheckBoxState()

        checkbox_popularity.isChecked = sharedPrefMovieFilter.loadSortByPopularityCheckBoxState()

    }

    private fun removeFocusFromEditText() {
        edit_text_release_year.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                edit_text_release_year.clearFocus()
            }
            return@setOnEditorActionListener false
        }
    }

    private fun openHomePage() {
        btn_submit.setOnClickListener {
            checkIfRatingIsMarked()
            checkIfReleaseYearIsMarked()
            checkIfGenreCheckBoxIsMarked()
            checkIfSortByPopularityIsMarked()
            findNavController().navigate(R.id.action_moviesFilterFragment_to_homeFragment)
        }
    }

    private fun checkIfRatingIsMarked() {
        if (checkbox_ratings.isChecked) {
            spinner_ratings.apply {
                viewModel.saveRatingSate(selectedItem.toString().toInt(), selectedItemPosition)
            }
            return
        }
        viewModel.clearRatingCache()
    }

    private fun checkIfReleaseYearIsMarked() {
        if (checkbox_release_year.isChecked) {
           viewModel.saveReleaseYearState(edit_text_release_year.text.toString())
            return
        }
        viewModel.clearReleaseYar()
    }

    private fun checkIfGenreCheckBoxIsMarked() {
        if (checkbox_genres.isChecked) {
            spinner_genres.apply {
                viewModel.saveGenreState(selectedItem.toString(), selectedItemPosition)
            }
            return
        }
        viewModel.clearGenreCache()
    }

    private fun checkIfSortByPopularityIsMarked() {
        if (checkbox_popularity.isChecked) {
            viewModel.saveSortByPopularityState()
            return
        }
        viewModel.clearSortByPopularityState()
    }
}