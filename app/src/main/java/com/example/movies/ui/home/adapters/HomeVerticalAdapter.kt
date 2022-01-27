package com.example.movies.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.MoviesSortedByGenreContainerModel
import com.example.movies.databinding.CellHomeVerticalBinding

class HomeVerticalAdapter : RecyclerView.Adapter<HomeVerticalAdapter.VerticalRVViewHolder>() {

    private val verticalItemsList = mutableListOf<MoviesSortedByGenreContainerModel>()

    private val _genreData = MutableLiveData<MoviesSortedByGenreContainerModel>()
    val genreData: LiveData<MoviesSortedByGenreContainerModel> get() = _genreData

    private val _clickedMovieId = MutableLiveData<Int>()
    val clickedMovieId: LiveData<Int> get() = _clickedMovieId

    fun setData(newList: List<MoviesSortedByGenreContainerModel>) {
        verticalItemsList.clear()
        verticalItemsList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerticalRVViewHolder {
        val binding = CellHomeVerticalBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VerticalRVViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VerticalRVViewHolder, position: Int) {
        val verticalItemModel = verticalItemsList[position]

        holder.bind(verticalItemModel)
        holder.onClickItem(verticalItemModel)
        holder.getMovieId()
    }

    override fun getItemCount(): Int {
        return verticalItemsList.size
    }

    inner class VerticalRVViewHolder(private val binding: CellHomeVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val homeHorizontalAdapter = HomeHorizontalAdapter()

        fun bind(rvVerticalModel: MoviesSortedByGenreContainerModel) {
            itemView.apply {
                binding.textGenre.text = rvVerticalModel.genreName
                binding.rvHorizontal.apply {
                    adapter = homeHorizontalAdapter
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                }
            }
            homeHorizontalAdapter.setData(rvVerticalModel.moviesList)
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