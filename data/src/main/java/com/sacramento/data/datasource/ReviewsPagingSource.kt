package com.sacramento.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sacramento.data.utils.START_PAGE
import com.sacramento.domain.models.ReviewModel
import com.sacramento.domain.repositories.MoviesRepository
import retrofit2.HttpException
import java.io.IOException

class ReviewsPagingSource(
    private val moviesRepository: MoviesRepository
): PagingSource<Int,ReviewModel>() {

    private var movieId = 0

    fun setupMovieId(id: Int) {
        movieId = id
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ReviewModel> {
       return try{
           val page = params.key ?: START_PAGE
           val movieReviews = moviesRepository.getReviews(page = page,movieId = movieId)

           LoadResult.Page(
               data =  movieReviews,
               prevKey = if (page == START_PAGE) null else page - 1,
               nextKey = if (movieReviews.isEmpty()) null else page + 1
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

    override fun getRefreshKey(state: PagingState<Int, ReviewModel>): Int? {
        return state.anchorPosition
    }
}