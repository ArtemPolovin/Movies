package com.sacramento.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sacramento.data.utils.START_PAGE
import com.sacramento.domain.models.MovieModel
import com.sacramento.domain.repositories.MoviesRepository
import retrofit2.HttpException
import java.io.IOException

class MoviesPagingSource(
    private val moviesRepository: MoviesRepository
) : PagingSource<Int, MovieModel>() {

    var movieName = ""

    fun setupMovieName(newMovieName: String) {
        movieName = newMovieName
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieModel> {

        return try {
            val page = params.key ?: START_PAGE
            val moviesList = moviesRepository.getMoviesByName(movieName = movieName, page = page)

            LoadResult.Page(
                data = moviesList,
                prevKey = if (page == START_PAGE) null else page - 1,
                nextKey = if (moviesList.isEmpty()) null else page + 1
            )
        } catch (e: IOException) {
            e.printStackTrace()
            LoadResult.Error(e)
        } catch (e: HttpException) {
            e.printStackTrace()
            LoadResult.Error(e)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieModel>): Int? {
        return state.anchorPosition
    }

}