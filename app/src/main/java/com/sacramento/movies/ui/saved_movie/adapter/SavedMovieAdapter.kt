package com.sacramento.movies.ui.saved_movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sacramento.domain.models.MovieModel
import com.sacramento.movies.R
import com.sacramento.movies.databinding.CellSavedMovieBinding
import com.sacramento.movies.ui.search_movie_by_name.adapters.MoviesDiffUtilCallback

class SavedMovieAdapter : PagingDataAdapter<MovieModel, SavedMovieAdapter.SavedMoviesViewHolder>(
    MoviesDiffUtilCallback()
) {

    private val elementsIdList = mutableListOf<Int>()
    private val _selectedElementsId = MutableLiveData<List<Int>>()
    val selectedElementsId: LiveData<List<Int>> get() = _selectedElementsId

    private val selectedItemsList = mutableListOf<Int>()

    var onItemClickListener: ((movieModel: MovieModel?) -> Unit)? = null


    fun clearSelectedElementsList() {
        elementsIdList.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedMoviesViewHolder {
        val binding: CellSavedMovieBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.cell_saved_movie, parent, false
        )
        return SavedMoviesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedMoviesViewHolder, position: Int) {
        getItem(position)?.let { movieModel ->
            holder.bind(movieModel)
            holder.click(movieModel)
            holder.longClick(movieModel.movieId, position)
        }
        holder.itemView.isSelected = selectedItemsList.contains(holder.layoutPosition)
    }

    inner class SavedMoviesViewHolder(private val binding: CellSavedMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movieModel: MovieModel) {
            binding.movieWithDetailsBindingModel = movieModel
        }

        fun longClick(movieId: Int, itemPosition: Int) {

            itemView.setOnLongClickListener {

                notifyItemChanged(itemPosition)
                selectedItemsList.add(layoutPosition)
                elementsIdList.add(movieId)
                _selectedElementsId.value = elementsIdList
                notifyItemChanged(layoutPosition)
                true
            }
        }

        fun click(movieModel: MovieModel) {

            itemView.setOnClickListener {
                if (itemView.isSelected) {
                    selectedItemsList.remove(layoutPosition)
                    elementsIdList.remove(movieModel.movieId)
                    _selectedElementsId.value = elementsIdList
                    itemView.isSelected = false
                } else {
                   onItemClickListener?.invoke(movieModel)
                }

            }
        }
    }


}