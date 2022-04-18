package com.example.movies.ui.watch_trailer

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.domain.models.TrailerModel
import com.example.movies.databinding.ActivityWatchTrailerBinding
import com.example.movies.ui.watch_trailer.adapter.ViewPagerThumbnailsAdapter
import com.example.movies.utils.BUNDLE_TRAILER_LIST_KEY
import com.example.movies.utils.getKSerializable
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.ExperimentalSerializationApi
import kotlin.math.abs

@ExperimentalSerializationApi
@AndroidEntryPoint
class WatchTrailerActivity : AppCompatActivity() {
    private var _binding: ActivityWatchTrailerBinding? = null
    private val binding: ActivityWatchTrailerBinding
        get() =
            _binding ?: throw RuntimeException("ActivityWatchTrailerBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityWatchTrailerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupWatchTrailerScreen()
        fullScreenListener()
    }

    private fun setupWatchTrailerScreen() {
        val trailerList = intent.extras?.getKSerializable<List<TrailerModel>>(
            BUNDLE_TRAILER_LIST_KEY)

        trailerList?.let {trailers ->
            showVideo(trailers[0].videoKey)

            if (trailers.size > 1) setupViewPager(trailers)
            else binding.groupTrailerPageView.visibility = View.GONE
        }
    }

    private fun setupViewPager(trailerList: List<TrailerModel>) {
        val thumbnailsAdapter = ViewPagerThumbnailsAdapter()
        thumbnailsAdapter.submitList(trailerList)

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(40))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }

        binding.viewPagerVideoThumbnails.apply {
            adapter = thumbnailsAdapter
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            setPageTransformer(compositePageTransformer)
        }

        thumbnailsAdapter.onItemClickListener = {videoId ->
            changeVideo(videoId)
        }

    }

    private fun showVideo(videoId: String?) {

        lifecycle.addObserver(binding.youtubePlayer)

        binding.youtubePlayer.addYouTubePlayerListener(object :
            AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                videoId?.let { youTubePlayer.loadVideo(it, 0f) }
            }
        })
    }

    private fun changeVideo(videoId: String?) {
        binding.youtubePlayer.getYouTubePlayerWhenReady(object:YouTubePlayerCallback{
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                videoId?.let { youTubePlayer.loadVideo(it, 0f) }
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