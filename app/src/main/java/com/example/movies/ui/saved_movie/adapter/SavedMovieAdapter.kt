package com.example.movies.ui.saved_movie.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.models.MovieModel
import com.example.movies.databinding.CellSavedMovieBinding

class SavedMovieAdapter : ListAdapter<MovieModel, SavedMovieAdapter.SavedMoviesViewHolder>(SavedMoviesDiffCallback()) {

    private val _selectedMovie = MutableLiveData<MovieModel>()
    val selectedMovie: LiveData<MovieModel> get() = _selectedMovie

    private val elementsIdList = mutableListOf<Int>()
    private val _selectedElementsId = MutableLiveData<List<Int>>()
    val selectedElementsId: LiveData<List<Int>> get() = _selectedElementsId

    init {
        setHasStableIds(true)
    }

    fun clearSelectedElementsList() {
        elementsIdList.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedMoviesViewHolder {
        val binding = CellSavedMovieBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SavedMoviesViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: SavedMoviesViewHolder, position: Int) {
       val movieModel = getItem(position)

        holder.bind(movieModel)
        holder.click(movieModel)
        holder.longClick(position)
        holder.itemView.isSelected = elementsIdList.contains(getItemId(position).toInt())
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).movieId.toLong()
    }

    inner class SavedMoviesViewHolder(
        private val binding: CellSavedMovieBinding,
        private val context: Context,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movieModel: MovieModel) {
            loadImage(binding.imageMoviePoster, movieModel.poster)
            binding.textMovieName.text = movieModel.title
            binding.textRating.text = movieModel.rating.toString()
            binding.textVoteCount.text = movieModel.voteCount.toString()
        }

       private fun loadImage(image: ImageView, imageUrl: String?) {
            Glide.with(context)
                .load(imageUrl)
                .into(image)
        }

        fun longClick(itemPosition: Int) {
            itemView.setOnLongClickListener {
                notifyItemChanged(itemPosition)
                elementsIdList.add(itemId.toInt())
                _selectedElementsId.value = elementsIdList
                notifyItemChanged(layoutPosition)
                true
            }
        }

        fun click(movieModel: MovieModel) {
            itemView.setOnClickListener {
                if (itemView.isSelected) {
                    elementsIdList.remove(itemId.toInt())
                    _selectedElementsId.value = elementsIdList
                    itemView.isSelected = false
                } else {
                    _selectedMovie.value = movieModel
                }

            }
        }
    }



}