package com.sacramento.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sacramento.data.utils.START_PAGE
import com.sacramento.domain.models.MovieModel
import com.sacramento.domain.repositories.MovieDBRepository
import com.sacramento.domain.repositories.MoviesRepository
import java.io.IOException

class MoviesPagingSourceDB(
    private val movieDBRepository: MovieDBRepository
) : PagingSource<Int, MovieModel>() {

    private var movieName = ""

    fun setupMovieName(newMovieName: String) {
        movieName = newMovieName
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieModel> {

        return try {
            val page = params.key ?: 0

            val moviesList = if (movieName.isBlank()) {
                movieDBRepository.getSavedMovies(
                    limit = params.loadSize,
                    offset = page * params.loadSize
                )
            }else{
                movieDBRepository.getMoviesByNameFromDB(
                    movieName = movieName,
                    limit = params.loadSize,
                    offset = page * params.loadSize
                )
            }


            LoadResult.Page(
                data = moviesList,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (moviesList.isEmpty()) null else page + 1
            )
        } catch (e: IOException) {
            e.printStackTrace()
            LoadResult.Error(e)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieModel>): Int? {
        return state.anchorPosition?.let{
            anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}