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
import com.example.movies.databinding.ActivityTrailerBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener

class Trailer : AppCompatActivity() {
    private var _binding: ActivityTrailerBinding? = null
    private val binding: ActivityTrailerBinding
        get() =
            _binding ?: throw RuntimeException("ActivityTrailerBinding == null")

    private val args: TrailerArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTrailerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fullScreenListener()
        showVideo(args.videoId)

    }


    private fun showVideo(videoId: String) {
        lifecycle.addObserver(binding.youtubePlayer)

        // binding.youtubePlayer.enterFullScreen()

//       val a =  binding.youtubePlayer.inflateCustomPlayerUi(R.layout.custop_player_ui)
//
//        val btn = a.findViewById<Button>(R.id.custom_button_ui)
//
//        btn.setOnClickListener { println("mLog: clicked") }

        val listener = binding.youtubePlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
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