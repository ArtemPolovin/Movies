package com.sacramento.movies.work_manager

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sacramento.data.cache.SharedPrefTrendingMovieId
import com.sacramento.domain.usecases.movie_usecase.GetMoviePosterUseCase
import com.sacramento.domain.usecases.movie_usecase.GetTrendingMovieUseCase
import com.sacramento.domain.utils.ResponseResult
import com.sacramento.movies.R
import com.sacramento.movies.notifications.TrendingMovieNotification
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class TrendingMovieWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val getTrendingMovieUseCase: GetTrendingMovieUseCase,
    private val getMoviePosterUseCase: GetMoviePosterUseCase,
    private val trendingMovieNotification: TrendingMovieNotification,
    private val sharedPref: SharedPrefTrendingMovieId
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        return withContext(Dispatchers.IO) {
            val responseResult = getTrendingMovieUseCase.execute()
            when (responseResult) {
                is ResponseResult.Success -> {

                    val movieId = responseResult.data.movieId

                    if (movieId != sharedPref.loadTrendingMovieId()) {
                        val btmImage = getImage(responseResult.data.poster)
                        val movieName = responseResult.data.title
                        trendingMovieNotification.showNotification(movieName,context,btmImage,movieId)
                        sharedPref.saveTrendingMovieId(movieId)
                    }

                    return@withContext Result.success()
                }

                is ResponseResult.Failure -> {
                    return@withContext Result.failure()
                }
                else -> return@withContext Result.failure()
            }

        }

    }

    private suspend fun getImage(url: String?): Bitmap {
       return url?.let {
           val response = getMoviePosterUseCase.execute(it)
           when (response) {
               is ResponseResult.Success -> {
                   BitmapFactory.decodeStream(response.data.byteStream())
               }
               else -> {
                   BitmapFactory.decodeResource(context.resources, R.drawable.image_user)
               }
           }
       }?:BitmapFactory.decodeResource(context.resources, R.drawable.image_user)
    }

}