package com.example.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.data.cache.MovieCategories
import com.example.data.utils.START_PAGE
import com.example.data.cache.SharedPrefMovieCategory
import com.example.data.cache.SharedPrefMovieFilter
import com.example.domain.models.MovieWithDetailsModel
import com.example.domain.repositories.MoviesRepository
import retrofit2.HttpException
import java.io.IOException
import java.lang.IllegalArgumentException


class MoviePagingSource(
    private val movieRepository: MoviesRepository,
    private val sharedPrefMovieCategory: SharedPrefMovieCategory,
    private val shardPrefMovieFilter: SharedPrefMovieFilter
) : PagingSource<Int, MovieWithDetailsModel>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieWithDetailsModel> {
        val page = params.key ?: START_PAGE
        val moviesWithDetailsList = mutableListOf<MovieWithDetailsModel>()
        val movieCategory = sharedPrefMovieCategory.loadMovieCategory()
        val genreId = sharedPrefMovieCategory.loadGenreId()
        val rating = shardPrefMovieFilter.loadRating()
        val releaseYear = shardPrefMovieFilter.loadReleaseYear()
        val sortByPopulation = shardPrefMovieFilter.loadSortByPopularity()

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
                    moviesWithDetailsList.addAll(movieRepository.getMoviesByGenre(genreId,rating,releaseYear,sortByPopulation))
                }


            }

            LoadResult.Page(
                data = moviesWithDetailsList,
                prevKey = if (page == START_PAGE) null else page - 1,
                nextKey = if (moviesWithDetailsList.isEmpty()) null else page + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }catch (e: IllegalArgumentException){
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieWithDetailsModel>): Int? {
        return state.anchorPosition
    }


}