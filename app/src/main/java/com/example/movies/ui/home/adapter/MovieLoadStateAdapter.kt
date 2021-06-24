package com.example.movies.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R

class MovieLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<MovieLoadStateAdapter.MovieLoadStateVieHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): MovieLoadStateVieHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_load_state_footer_view_item, parent, false)
        return MovieLoadStateVieHolder(view, retry)
    }


    override fun onBindViewHolder(holder: MovieLoadStateVieHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class MovieLoadStateVieHolder(itemView: View, retry: () -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val buttonRetry = itemView.findViewById<Button>(R.id.retry_button)
        private val textError = itemView.findViewById<TextView>(R.id.error_msg)
        private val progressBar = itemView.findViewById<ProgressBar>(R.id.progress_bar)

        init {
            buttonRetry.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                textError.text = loadState.error.localizedMessage
            }
            progressBar.isVisible = loadState is LoadState.Loading
            buttonRetry.isVisible = loadState is LoadState.Error
            textError.isVisible = loadState is LoadState.Error
        }
    }
}