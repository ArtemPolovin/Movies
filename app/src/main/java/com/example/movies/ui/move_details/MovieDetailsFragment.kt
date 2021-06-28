package com.example.movies.ui.move_details

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.domain.models.PopularMovieWithDetailsModel
import com.example.movies.R
import com.example.movies.ui.MainActivity
import com.example.movies.utils.getKSerializable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_movie_details.*
import kotlinx.android.synthetic.main.fragment_movie_details.toolbar

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private val viewModel: MovieDetailsViewModel by viewModels()

    private var movieWithDetails: PopularMovieWithDetailsModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.fragment_movie_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        (requireActivity() as MainActivity).setupActionBar(toolbar, false)

        movieWithDetails = arguments?.getKSerializable<PopularMovieWithDetailsModel>("movieObject")
        movieWithDetails?.let { setupMovieDetails(it, requireContext()) }

        openWebHomePage()
    }

    private fun setupMovieDetails(movieModel: PopularMovieWithDetailsModel, context: Context) {
        movieModel.apply {
            text_movie_name_details.text = movieName
            text_popularity.text = popularityScore
            text_release_date.text = releaseData
            text_overview.text = overview
            rating_bar_movie.rating = rating.toFloat()
            text_rating_details.text = rating.toString()
            loadImage(context, image_movie_details, backdropPoster)
            text_genre.text = genres
            underlineText(text_homepage_url,homePageUrl)
        }

    }

    private fun loadImage(context: Context, image: ImageView, imageUrl: String?) {
        Glide.with(context)
            .load(imageUrl)
            .into(image)
    }

    private fun underlineText(textView: TextView, text: String) {
        val content = SpannableString(text)
        content.setSpan(UnderlineSpan(), 0, text.length, 0)
        textView.text = content
    }

    private fun openWebHomePage() {
        text_homepage_url.setOnClickListener {
            val  browserIntent =  Intent(Intent.ACTION_VIEW, Uri.parse(text_homepage_url.text.toString()))
            startActivity(browserIntent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.details_screen_tool_bar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.save_movie -> {
                movieWithDetails?.let { viewModel.insertMovieToDb(it) }
                Toast.makeText(requireContext(), "The movie was saved", Toast.LENGTH_LONG).show()
                 true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}