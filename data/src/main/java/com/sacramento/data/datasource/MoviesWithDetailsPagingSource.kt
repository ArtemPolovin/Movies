package com.sacramento.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sacramento.data.cache.MovieCategories
import com.sacramento.data.utils.MovieFilterParams
import com.sacramento.data.utils.START_PAGE
import com.sacramento.domain.models.MovieWithDetailsModel
import com.sacramento.domain.repositories.MoviesRepository
import retrofit2.HttpException
import java.io.IOException
import java.lang.IllegalArgumentException


class MoviesWithDetailsPagingSource(
    private val movieRepository: MoviesRepository
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
                    moviesWithDetailsList.addAll(movieRepository.getUpcomingMoviesWithDetails(page,rating,releaseYear))
                }
                MovieCategories.TopRated.category -> {
                    moviesWithDetailsList.addAll(movieRepository.getTopRatedMoviesWithDetails(page,rating,releaseYear))
                }
                MovieCategories.Popular.category -> {
                    moviesWithDetailsList.addAll(movieRepository.getPopularMoviesWithDetails(page,rating,releaseYear))
                }
                else -> {
                    moviesWithDetailsList.addAll(movieRepository.getMoviesByGenre(genreId,rating,releaseYear,sortByPopulation,page))
                }


            }

            //ORDER BY CASE WHEN :sorted NOT NULL THEN rating  END DESC

            LoadResult.Page(
                data = moviesWithDetailsList,
                prevKey = if (page == START_PAGE) null else page - 1,
                nextKey = if (moviesWithDetailsList.isEmpty()) null else page + 1
            )
        } catch (e: IOException) {
            e.printStackTrace()
            LoadResult.Error(e)
        } catch (e: HttpException) {
            e.printStackTrace()
            LoadResult.Error(e)
        }catch (e: IllegalArgumentException){
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieWithDetailsModel>): Int? {
        return state.anchorPosition
    }

}