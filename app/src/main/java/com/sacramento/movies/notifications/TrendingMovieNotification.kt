package com.sacramento.movies.notifications

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.sacramento.movies.R
import com.sacramento.movies.utils.CHANNEL_ID
import javax.inject.Inject

class TrendingMovieNotification @Inject constructor () {

      fun showNotification(movieName: String?, context: Context,btm:Bitmap, movieId: Int) {

          val bundle = Bundle().apply {
              putInt("movieId",movieId)
          }
          val pendingIntent = NavDeepLinkBuilder(context)
              .setGraph(R.navigation.mobile_navigation)
              .addDestination(R.id.homeFragment)
              .addDestination(R.id.Movie_details)
              .setArguments(bundle)
              .createPendingIntent()

        val  builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
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
