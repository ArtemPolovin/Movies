package com.sacramento.domain.usecases.movie_usecase

import com.sacramento.domain.models.ReviewModel
import com.sacramento.domain.repositories.MoviesRepository
import com.sacramento.domain.utils.ResponseResult
import java.io.IOException
import java.lang.IllegalArgumentException
import java.lang.IndexOutOfBoundsException
import java.lang.NullPointerException

class GetMovieReviewsUseCase(private val moviesRepository: MoviesRepository) {
    suspend fun execute(page: Int, movieId: Int): ResponseResult<List<ReviewModel>> {
        return try {
            val reviews = moviesRepository.getReviews(page, movieId)
            return if (reviews.isNotEmpty()) {
                ResponseResult.Success(reviews)
            } else ResponseResult.Failure(message = "The review list is empty")
        } catch (e: IOException) {
            e.printStackTrace()
            ResponseResult.Failure(message = e.message ?: "The response is not successful")
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
            ResponseResult.Failure(message = e.message ?: "The response is not successful")
        }
    }
}