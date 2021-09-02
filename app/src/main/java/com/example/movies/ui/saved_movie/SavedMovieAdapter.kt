package com.example.movies.ui.saved_movie

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.models.MovieWithDetailsModel
import com.example.movies.R
import com.example.movies.utils.OnClickAdapterPopularMovieListener
import kotlinx.android.synthetic.main.cell_movie.view.image_movie_poster
import kotlinx.android.synthetic.main.cell_movie.view.text_movie_name
import kotlinx.android.synthetic.main.cell_movie.view.text_rating

class SavedMovieAdapter : RecyclerView.Adapter<SavedMovieAdapter.SavedMoviesViewHolder>() {

    private val savedMoviesList = mutableListOf<MovieWithDetailsModel>()

    private lateinit var onclickAdapterPopularMovieListener: OnClickAdapterPopularMovieListener
    //private lateinit var onLongClickAdapterItem: OnLongClickAdapterItme

    private val elementsIdList = mutableListOf<Int>()
    private val _selectedElementsId = MutableLiveData<List<Int>>()
    val selectedElementsId: LiveData<List<Int>>get() = _selectedElementsId


    init {
        setHasStableIds(true)
    }

    fun onclick(onclickAdapterPopularMovieListener: OnClickAdapterPopularMovieListener) {
        this.onclickAdapterPopularMovieListener = onclickAdapterPopularMovieListener
    }

//    fun onLongClick(onLongClickAdapterItem: OnLongClickAdapterItme) {
//        this.onLongClickAdapterItem = onLongClickAdapterItem
//    }

    fun clearSelectedElementsList() {
        elementsIdList.clear()
    }

    fun setupList(newSavedMovieList: List<MovieWithDetailsModel>) {
        savedMoviesList.clear()
        savedMoviesList.addAll(newSavedMovieList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedMoviesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cell_saved_movie, parent, false)
        return SavedMoviesViewHolder(
            view,
            parent.context,
            onclickAdapterPopularMovieListener,
            //onLongClickAdapterItem
        )
    }

    override fun onBindViewHolder(holder: SavedMoviesViewHolder, position: Int) {

        val movieModel = savedMoviesList[position]
        holder.itemView.apply {
            holder.loadImage(image_movie_poster, movieModel.poster)
            text_movie_name.text = movieModel.movieName
            text_rating.text = movieModel.rating.toString()
        }

        holder.click(movieModel)
        holder.longClick(position)

        holder.itemView.isSelected = elementsIdList.contains(getItemId(position).toInt())

    }

    override fun getItemCount(): Int {
        return savedMoviesList.size
    }

    override fun getItemId(position: Int): Long {
        return savedMoviesList[position].id.toLong()
    }

    inner class SavedMoviesViewHolder(
        itemView: View,
        private val context: Context,
        private val onclickAdapterPopularMovieListener: OnClickAdapterPopularMovieListener,
       // private val onLongClickAdapterItem: OnLongClickAdapterItme
    ) : RecyclerView.ViewHolder(itemView) {

        fun loadImage(image: ImageView, imageUrl: String?) {
            Glide.with(context)
                .load(imageUrl)
                .into(image)
        }

        fun longClick(itemPosition: Int) {
            itemView.setOnLongClickListener {
                notifyItemChanged(itemPosition)
                elementsIdList.add(itemId.toInt())
                _selectedElementsId.value = elementsIdList
              //  onLongClickAdapterItem.getMoviePositionAndId(itemPosition, elementsIdList)
                true
            }
        }

        fun click(movieModel: MovieWithDetailsModel) {
            itemView.setOnClickListener {
                if(itemView.isSelected){
                    elementsIdList.remove(itemId.toInt())
                    _selectedElementsId.value = elementsIdList
                    itemView.isSelected = false
                }else{
                    onclickAdapterPopularMovieListener.getPopularMovie(movieModel)
                }

            }
        }
    }


}