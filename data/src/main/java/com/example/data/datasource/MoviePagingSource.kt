package com.example.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.data.utils.START_PAGE
import com.example.domain.models.PopularMovieWithDetailsModel
import com.example.domain.repositories.MoviesRepository
import retrofit2.HttpException
import java.io.IOException


class MoviePagingSource(
   private val movieRepository: MoviesRepository
    ): PagingSource<Int, PopularMovieWithDetailsModel>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PopularMovieWithDetailsModel> {
        val page = params.key ?: START_PAGE

        return try {
            val moviesWithDetails = movieRepository.getMoviesWithDetails(page)

            LoadResult.Page(
                data = moviesWithDetails,
                prevKey = if (page == START_PAGE) null else page - 1,
                nextKey = if (moviesWithDetails.isEmpty()) null else page + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PopularMovieWithDetailsModel>): Int? {
        return state.anchorPosition
    }


}