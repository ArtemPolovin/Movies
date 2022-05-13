package com.example.movies.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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

          val bundle = Bundle().apply {
              putInt("movieId",movieId)
              putBoolean("isNotification",true)
          }
          val pendingIntent = NavDeepLinkBuilder(context)
              .setGraph(R.navigation.mobile_navigation)
              .addDestination(R.id.homeFragment)
              .addDestination(R.id.Movie_details)
              .setArguments(bundle)
              .createPendingIntent()


      /*    val intent = Intent(context,MainActivity::class.java).apply {
              flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
              putExtra("movieId",movieId)
              putExtra("isNotification",true)
          }
          val pendingIntent = PendingIntent.getActivity(context,0,intent,0)*/

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
