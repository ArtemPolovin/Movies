package com.sacramento.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sacramento.data.cache.MovieCategories
import com.sacramento.data.utils.MovieFilterParams
import com.sacramento.data.utils.START_PAGE
import com.sacramento.domain.models.MovieWithDetailsModel
import com.sacramento.domain.repositories.MovieDBRepository
import java.io.IOException

class MoviesWithDetailsPagingSourceDB(
    private val movieDBRepository: MovieDBRepository
) : PagingSource<Int, MovieWithDetailsModel>() {

    private var movieFilterParams: MovieFilterParams? = null

    fun setUpFilter(newFilterParams: MovieFilterParams) {
        movieFilterParams = newFilterParams
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieWithDetailsModel> {
        val page = params.key ?: START_PAGE
        val moviesWithDetailsList = mutableListOf<MovieWithDetailsModel>()
        val movieCategory = movieFilterParams?.movieCategory
        val genreId = movieFilterParams?.genreId
        val rating = movieFilterParams?.rating
        val releaseYear = movieFilterParams?.releaseYear
        val sortByPopulation = movieFilterParams?.sortByPopularity

        return try {
                when (movieCategory) {
                    MovieCategories.Upcoming.category -> {
                        moviesWithDetailsList.addAll(movieDBRepository.getUpcomingMovies())
                    }
                    MovieCategories.TopRated.category -> {
                        moviesWithDetailsList.addAll(movieDBRepository.getTopRatedMovies())
                    }
                    MovieCategories.Popular.category -> {
                        moviesWithDetailsList.addAll(movieDBRepository.getMoviesFromDBSortedByPopularity())
                    }
                    else -> {
                        moviesWithDetailsList.addAll(
                            movieDBRepository.getFilteredMovies(
                                genreId = genreId,
                                year = releaseYear,
                                rating = rating?.toFloat(),
                                sortedByPopularity = sortByPopulation,
                                limit = params.loadSize
                            )
                        )
                    }

                }

            LoadResult.Page(
                data = moviesWithDetailsList,
                prevKey = if (page == START_PAGE) null else page - 1,
                nextKey = if (moviesWithDetailsList.isEmpty()) null else page + 1
            )
        } catch (e: IOException) {
            e.printStackTrace()
            LoadResult.Error(e)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieWithDetailsModel>): Int? {
        return state.anchorPosition
    }

}