package com.sacramento.movies.work_manager

import android.content.Context
import androidx.lifecycle.LifecycleObserver
import androidx.work.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TrendingMovieWorkerScheduler @Inject constructor(): LifecycleObserver {

    fun startTrendingMovieRequest(context: Context) {

        val workManager = WorkManager.getInstance(context)

        val trendingMovieRequest =
            PeriodicWorkRequest.Builder(TrendingMovieWorker::class.java, 24, TimeUnit.HOURS)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()

        workManager.enqueueUniquePeriodicWork(
            "show trending movie in notification",
            ExistingPeriodicWorkPolicy.KEEP,
            trendingMovieRequest
        )

      // WorkManager.getInstance(context).cancelAllWork()

    }
}