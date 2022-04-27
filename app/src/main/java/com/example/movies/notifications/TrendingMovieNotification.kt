package com.example.movies.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.NavDeepLinkBuilder
import com.example.movies.R
import com.example.movies.ui.MainActivity
import com.example.movies.ui.move_details.MovieDetailsFragment
import com.example.movies.utils.CHANNEL_ID
import javax.inject.Inject

class TrendingMovieNotification @Inject constructor () {

      fun showNotification(movieName: String?, context: Context,btm:Bitmap, movieId: Int) {

          val bundle = Bundle()
          bundle.putInt("movieId",movieId)

          val pendingIntent = NavDeepLinkBuilder(context)
              .setGraph(R.navigation.mobile_navigation)
              .setDestination(R.id.Movie_details)
              .setArguments(bundle)
              .createPendingIntent()

        val  builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_movie)
            .setContentText(movieName)
            .setContentTitle("New leader")
            .setLargeIcon(btm)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigPictureStyle()
                .bigPicture(btm)
                .bigLargeIcon(null)
            )

        with(NotificationManagerCompat.from(context)){
            notify(123,builder.build())
        }
    }

}
