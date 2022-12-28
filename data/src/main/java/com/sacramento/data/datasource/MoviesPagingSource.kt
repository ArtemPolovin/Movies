package com.sacramento.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sacramento.data.utils.START_PAGE
import com.sacramento.domain.models.MovieModel
import com.sacramento.domain.repositories.MoviesRepository
import com.sacramento.domain.usecases.movie_usecase.GetWatchListUseCase
import retrofit2.HttpException
import java.io.IOException

class MoviesPagingSource(
    private val moviesRepository: MoviesRepository,
    private val getWatchListUseCase: GetWatchListUseCase
) : PagingSource<Int, MovieModel>() {

    private var movieName = ""

    fun setupMovieName(newMovieName: String) {
        movieName = newMovieName
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieModel> {

        return try {
            val page = params.key ?: START_PAGE

            val moviesList = if (movieName.isBlank()) {
                getWatchListUseCase.execute(page)
            } else {
                moviesRepository.getMoviesByName(movieName = movieName, page = page)
            }

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