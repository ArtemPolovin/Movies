package com.sacramento.movies.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sacramento.domain.models.MoviesSortedByGenreContainerModel
import com.sacramento.movies.R
import com.sacramento.movies.databinding.CellHomeVerticalBinding

class HomeVerticalAdapter :
    ListAdapter<MoviesSortedByGenreContainerModel, HomeVerticalAdapter.VerticalRVViewHolder>(
        SortedMoviesByGenreDiffCallback()
    ) {

    private val _genreData = MutableLiveData<MoviesSortedByGenreContainerModel>()
    val genreData: LiveData<MoviesSortedByGenreContainerModel> get() = _genreData

    private val _clickedMovieId = MutableLiveData<Int>()
    val clickedMovieId: LiveData<Int> get() = _clickedMovieId

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerticalRVViewHolder {
        val binding = CellHomeVerticalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VerticalRVViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VerticalRVViewHolder, position: Int) {
        val verticalItemModel = getItem(position)

        holder.bind(verticalItemModel)
        holder.onClickItem(verticalItemModel)
        holder.getMovieId()
    }

    inner class VerticalRVViewHolder(val binding: CellHomeVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val homeHorizontalAdapter = HomeHorizontalAdapter()

        fun bind(rvVerticalModel: MoviesSortedByGenreContainerModel) {
            binding.textGenre.text = rvVerticalModel.genreName

            itemView.apply {
                binding.rvHorizontal.apply {
                    adapter = homeHorizontalAdapter
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                }
            }
            homeHorizontalAdapter.submitList(rvVerticalModel.moviesList)
        }

        fun onClickItem(rvVerticalModel: MoviesSortedByGenreContainerModel) {
            binding.titleContainer.setOnClickListener {
                _genreData.value = rvVerticalModel
            }
        }

        fun getMovieId() {
            homeHorizontalAdapter.onItemClickListener = { movieId ->
                _clickedMovieId.value = movieId
            }
        }
    }
}