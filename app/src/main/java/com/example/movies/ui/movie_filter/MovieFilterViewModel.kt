package com.example.movies.ui.movie_filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.cache.SharedPrefMovieCategory
import com.example.data.cache.SharedPrefMovieFilter
import com.example.domain.models.GenreModel
import com.example.domain.usecases.movie_usecase.GetGenresUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieFilterViewModel @Inject constructor(
    private val getGenresUseCase: GetGenresUseCase,
    private val sharedPrefMovieCategory: SharedPrefMovieCategory,
    private val sharedPrefMovieFilter: SharedPrefMovieFilter
) : ViewModel() {

    private val _genreNames = MutableLiveData<List<String>>()
    val genreNames: LiveData<List<String>> get() = _genreNames

    private val movieGenresList = mutableListOf<GenreModel>()

    init {
        fetchMovieGenresList()
    }

    private fun fetchMovieGenresList() {
        viewModelScope.launch {
            val genreNamesList = mutableListOf<String>()
            movieGenresList.addAll(getGenresUseCase.execute())

            movieGenresList.forEach {
                genreNamesList.add(it.name)
            }
            _genreNames.value = genreNamesList
        }
    }

    fun saveRatingSate(ratingNumber: Int, ratingPosition: Int) {
        sharedPrefMovieFilter.saveRating(ratingNumber)
        sharedPrefMovieFilter.saveRatingPosition(ratingPosition)
        sharedPrefMovieFilter.saveRatingCheckBoxState(true)
    }

    fun clearRatingCache() {
        sharedPrefMovieFilter.clearRating()
        sharedPrefMovieFilter.clearRatingPosition()
        sharedPrefMovieFilter.saveRatingCheckBoxState(false)
    }

    fun saveReleaseYearState(year: String) {
        sharedPrefMovieFilter.saveReleaseYear(year)
        sharedPrefMovieFilter.saveReleaseYearCheckBoxState(true)
    }

    fun clearReleaseYar() {
        sharedPrefMovieFilter.clearReleaseYar()
        sharedPrefMovieFilter.saveReleaseYearCheckBoxState(false)
    }

    fun saveGenreState(genreName: String, genreSpinnerPosition: Int) {
        movieGenresList.forEach {
            if (it.name == genreName) {
                sharedPrefMovieCategory.saveGenreId(it.id)
                sharedPrefMovieCategory.saveMovieCategory(it.name)
                sharedPrefMovieFilter.saveGenreSpinnerPosition(genreSpinnerPosition)
                sharedPrefMovieFilter.saveGenreCheckBoxState(true)
            }
        }
    }

    fun clearGenreCache() {
        sharedPrefMovieFilter.clearGenreSpinnerPosition()
        sharedPrefMovieFilter.saveGenreCheckBoxState(false)
    }

    fun saveSortByPopularityState() {
        sharedPrefMovieFilter.saveSortByPopularity()
        sharedPrefMovieFilter.saveSortByPopularityCheckBoxState(true)
    }

    fun clearSortByPopularityState() {
        sharedPrefMovieFilter.clearSortByPopularity()
        sharedPrefMovieFilter.saveSortByPopularityCheckBoxState(false)
    }

}