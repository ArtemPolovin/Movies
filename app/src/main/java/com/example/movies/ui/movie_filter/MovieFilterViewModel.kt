package com.example.movies.ui.movie_filter

import androidx.lifecycle.ViewModel
import com.example.data.cache.SharedPrefMovieCategory
import com.example.data.cache.SharedPrefMovieFilter
import com.example.domain.usecases.movie_usecase.GetGenresUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.android.synthetic.main.fragment_movies_filter.*
import javax.inject.Inject

@HiltViewModel
class MovieFilterViewModel @Inject constructor(
    private val getGenresUseCase: GetGenresUseCase,
    private val sharedPrefMovieCategory: SharedPrefMovieCategory,
    private val sharedPrefMovieFilter: SharedPrefMovieFilter
) : ViewModel() {

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
        getGenresUseCase.execute().forEach {
            if (it.name == genreName) {
                sharedPrefMovieCategory.saveGenreId(it.id.toString())
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