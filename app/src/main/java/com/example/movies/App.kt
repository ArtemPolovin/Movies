package com.example.movies

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.example.movies.utils.CHANNEL_ID
import com.example.movies.work_manager.TrendingMovieWorker
import com.example.movies.work_manager.TrendingMovieWorkerScheduler
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class App: Application(), Configuration.Provider {

    @Inject
     lateinit var trendingMovieWorkerScheduler: TrendingMovieWorkerScheduler

     @Inject
     lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        trendingMovieWorkerScheduler.startTrendingMovieRequest(applicationContext)
    }

    override fun getWorkManagerConfiguration(): Configuration {
       return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "Trending movie",
                NotificationManager.IMPORTANCE_HIGH
            )

           val  notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)

        }
    }


}