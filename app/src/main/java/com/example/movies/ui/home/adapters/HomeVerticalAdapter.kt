package com.example.movies.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.MoviesSortedByGenreContainerModel
import com.example.movies.R
import com.example.movies.databinding.CellHomeVerticalBinding

class HomeVerticalAdapter :
    ListAdapter<MoviesSortedByGenreContainerModel, HomeVerticalAdapter.VerticalRVViewHolder>(
        SortedMoviesByGenreDiffCallback()
    ) {

    private val _genreData = MutableLiveData<MoviesSortedByGenreContainerModel>()
    val genreData: LiveData<MoviesSortedByGenreContainerModel> get() = _genreData

    private val _clickedMovieId = MutableLiveData<Int>()
    val clickedMovieId: LiveData<Int> get() = _clickedMovieId

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerticalRVViewHolder {
        val binding: CellHomeVerticalBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.cell_home_vertical,
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

    inner class VerticalRVViewHolder(binding: CellHomeVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val sortedMoviesByGenreBinding = binding
        private val homeHorizontalAdapter = HomeHorizontalAdapter()

        fun bind(rvVerticalModel: MoviesSortedByGenreContainerModel) {

            sortedMoviesByGenreBinding.sortedByGenreMovieBindingModel = rvVerticalModel

            itemView.apply {
                sortedMoviesByGenreBinding.rvHorizontal.apply {
                    adapter = homeHorizontalAdapter
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                }
            }
            homeHorizontalAdapter.submitList(rvVerticalModel.moviesList)
        }

        fun onClickItem(rvVerticalModel: MoviesSortedByGenreContainerModel) {
            sortedMoviesByGenreBinding.titleContainer.setOnClickListener {
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