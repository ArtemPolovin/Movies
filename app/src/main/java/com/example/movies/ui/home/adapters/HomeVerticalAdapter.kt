package com.example.movies.ui.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.MoviesSortedByGenreContainerModel
import com.example.movies.R
import kotlinx.android.synthetic.main.cell_home_vertical.view.*

class HomeVerticalAdapter : RecyclerView.Adapter<HomeVerticalAdapter.VerticalRVViewHolder>() {

    private val verticalItemsList = mutableListOf<MoviesSortedByGenreContainerModel>()

    private val _genreData = MutableLiveData<MoviesSortedByGenreContainerModel>()
    val genreData: LiveData<MoviesSortedByGenreContainerModel>get() = _genreData

    private val _clickedMovieId = MutableLiveData<Int>()
    val clickedMovieId: LiveData<Int>get() = _clickedMovieId

    fun setData(newList: List<MoviesSortedByGenreContainerModel>) {
        verticalItemsList.clear()
        verticalItemsList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerticalRVViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cell_home_vertical, parent, false)
        return VerticalRVViewHolder(view)
    }

    override fun onBindViewHolder(holder: VerticalRVViewHolder, position: Int) {
        val verticalItemModel = verticalItemsList[position]

        println("mLog: position = $position ,, name = ${verticalItemModel.genreName}")

        holder.bind(verticalItemModel)
        holder.onClickItem(verticalItemModel)
        holder.getMovieId()
    }

    override fun getItemCount(): Int {
        return verticalItemsList.size
    }

    inner class VerticalRVViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val homeHorizontalAdapter =  HomeHorizontalAdapter()

        fun bind(rvVerticalModel: MoviesSortedByGenreContainerModel) {
            itemView.apply {
                text_genre.text = rvVerticalModel.genreName
                rv_horizontal.apply {
                    adapter = homeHorizontalAdapter
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                }
            }
            homeHorizontalAdapter.setData(rvVerticalModel.moviesList)
        }

        fun onClickItem(rvVerticalModel: MoviesSortedByGenreContainerModel) {
            itemView.title_container.setOnClickListener {
                _genreData.value = rvVerticalModel
            }
        }

         fun getMovieId() {
            homeHorizontalAdapter.onItemClick(object : HomeHorizontalAdapter.OnItemClickListener{
                override fun getClickedMovieId(movieId: Int) {
                    _clickedMovieId.value = movieId
                }

            })
        }
    }
}