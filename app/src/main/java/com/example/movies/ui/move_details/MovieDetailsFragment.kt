package com.example.movies.ui.move_details

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.*
import android.view.View.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.domain.models.MovieWithDetailsModel
import com.example.movies.R
import com.example.movies.ui.MainActivity
import com.example.movies.utils.getKSerializable
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_movie_details.*

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private val viewModel: MovieDetailsViewModel by viewModels()

    private var movieWithDetails: MovieWithDetailsModel? = null

    private var showMenuSaveIcon: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.fragment_movie_details, container, false)
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        (requireActivity() as MainActivity).setupActionBar(toolbar, false)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        showMenuSaveIcon = arguments?.getBoolean("showSavedIcon") ?: false


        movieWithDetails = arguments?.getKSerializable<MovieWithDetailsModel>("movieObject")
        movieWithDetails?.let { setupMovieDetails(it) }

        openWebHomePage()
        fullScreenListener()
    }

    private fun setupMovieDetails(movieModel: MovieWithDetailsModel) {

        youtube_player.visibility = GONE
        image_movie_details.visibility = GONE

        movieModel.apply {
            text_movie_name_details.text = movieName
            text_popularity.text = popularityScore
            text_release_date.text = releaseData
            text_overview.text = overview
            text_vote_count_details.text = "(${voteCount})"
            rating?.let { rating_bar_movie.rating = it.toFloat() }
            text_rating_details.text = rating.toString()
            text_genre.text = genres
            homePageUrl?.let { underlineText(text_homepage_url, it) }
            showVideoOrPoster(movieModel)
        }
    }

    private fun showVideoOrPoster(movieModel: MovieWithDetailsModel) {
       movieModel.video?.let { video ->
           if (video.isNotBlank()) {
               youtube_player.visibility = VISIBLE
               showVideo(video)
           } else {
               image_movie_details.visibility = VISIBLE

               Glide.with(requireActivity())
                   .load(movieModel.backdropPoster)
                   .into(image_movie_details)
           }
       }

    }

    private fun showVideo(videoId: String) {
        viewLifecycleOwner.lifecycle.addObserver(youtube_player)

        youtube_player.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.cueVideo(videoId, 0f)
            }
        })
    }

    private fun underlineText(textView: TextView, text: String) {
        val content = SpannableString(text)
        content.setSpan(UnderlineSpan(), 0, text.length, 0)
        textView.text = content
    }

    private fun openWebHomePage() {
        text_homepage_url.setOnClickListener {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(text_homepage_url.text.toString()))
            startActivity(browserIntent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.details_screen_tool_bar_menu, menu)
        val item: MenuItem = menu.findItem(R.id.save_movie)
        item.isVisible = showMenuSaveIcon
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_movie -> {
                movieWithDetails?.let { viewModel.insertMovieToDb(it) }
                Toast.makeText(requireContext(), "The movie was saved", Toast.LENGTH_LONG).show()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun fullScreenListener() {

        val decorView = activity?.window?.decorView?.let {
            val screenListener = object : YouTubePlayerFullScreenListener {
                override fun onYouTubePlayerEnterFullScreen() {
                    youtube_player.enterFullScreen()
                    hideSystemUi(it)
                }

                override fun onYouTubePlayerExitFullScreen() {
                    showSystemUi(it)
                }

            }
            youtube_player.addFullScreenListener(screenListener)
        }

    }

    private fun hideSystemUi(view: View) {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        (requireActivity() as MainActivity).bottom_nav.visibility = GONE
        toolbar.visibility = GONE

        view.systemUiVisibility =
                SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                SYSTEM_UI_FLAG_FULLSCREEN or
                SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun showSystemUi(view: View) {

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        view.systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_STABLE or SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        (requireActivity() as MainActivity).bottom_nav.visibility = VISIBLE
        toolbar.visibility = VISIBLE
    }

    override fun onStop() {
        super.onStop()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER
    }

}