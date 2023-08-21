package com.sacramento.movies.ui.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sacramento.data.datasource.ReviewsPagingSource
import com.sacramento.domain.models.ReviewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class AllReviewsViewModel @Inject constructor(
    private val reviewsPagingSource: ReviewsPagingSource
): ViewModel() {

    private var lastFetchedReviewsResultFromServer: Flow<PagingData<ReviewModel>>? = null

    fun getReviewsFromServer(movieId: Int) : Flow<PagingData<ReviewModel>>{
        reviewsPagingSource.setupMovieId(movieId)
        val lastReviewsResult = lastFetchedReviewsResultFromServer
        if(lastReviewsResult != null) return lastReviewsResult

        val newFetchedReviewsResult = Pager(config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
            pagingSourceFactory = {reviewsPagingSource}
        ).flow.cachedIn(viewModelScope)

        lastFetchedReviewsResultFromServer = newFetchedReviewsResult

        return newFetchedReviewsResult
    }

}