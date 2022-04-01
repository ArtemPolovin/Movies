package com.example.movies.ui.watch_trailer

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.navArgs
import com.example.movies.databinding.ActivityWatchTrailerBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener

class WatchTrailerActivity : AppCompatActivity() {
    private var _binding: ActivityWatchTrailerBinding? = null
    private val binding: ActivityWatchTrailerBinding
        get() =
            _binding ?: throw RuntimeException("ActivityWatchTrailerBinding == null")

    private val args: WatchTrailerActivityArgs by navArgs()

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)

        _binding = ActivityWatchTrailerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fullScreenListener()
        showVideo(args.videoId)

    }

    private fun showVideo(videoId: String) {
        lifecycle.addObserver(binding.youtubePlayer)

        val listener = binding.youtubePlayer.addYouTubePlayerListener(object :
            AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(videoId, 0f)
            }
        })
    }

    private fun fullScreenListener() {
        val decorView = this.window?.decorView?.let {
            val screenListener = object : YouTubePlayerFullScreenListener {
                override fun onYouTubePlayerEnterFullScreen() {
                    binding.youtubePlayer.enterFullScreen()
                    hideSystemUi(it)
                }

                override fun onYouTubePlayerExitFullScreen() {
                    showSystemUi(it)
                }

            }
            binding.youtubePlayer.addFullScreenListener(screenListener)
        }

    }

    private fun hideSystemUi(view: View) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        WindowCompat.setDecorFitsSystemWindows(this.window, false)
        WindowInsetsControllerCompat(this.window, view).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun showSystemUi(view: View) {

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        WindowCompat.setDecorFitsSystemWindows(this.window, true)
        WindowInsetsControllerCompat(
            this.window,
            view
        ).show(WindowInsetsCompat.Type.systemBars())

    }

}