package com.sacramento.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sacramento.domain.models.MovieModel
import com.sacramento.domain.repositories.GuestSessionDbRepository
import java.io.IOException

class GuestSessionPagingSource(
    private val guestRepo: GuestSessionDbRepository
) : PagingSource<Int, MovieModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieModel> {
        return try {
            val page = params.key ?: 0

            val moviesList = guestRepo.getAllGuestMovies(
                limit = params.loadSize,
                offset = page * params.loadSize
            )


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
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }


}