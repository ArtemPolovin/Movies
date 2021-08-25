package com.example.movies.ui.saved_movie

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.domain.models.PopularMovieWithDetailsModel
import com.example.domain.models.utils.ResponseResult
import com.example.movies.R
import com.example.movies.ui.MainActivity
import com.example.movies.utils.OnClickAdapterPopularMovieListener
import com.example.movies.utils.OnLongClickAdapterItme
import com.example.movies.utils.putKSerializable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_saved_movie.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SavedMovieFragment : Fragment() {

    private val viewModel: SavedMovieViewModel by viewModels()
    private lateinit var savedMoviesAdapter: SavedMovieAdapter

    private var showDeleteMenuIcon = MutableLiveData<Boolean>()
   // private var movieAdapterPosition = 0
    private var popularMovieId  = mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_saved_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        (requireActivity() as MainActivity).setupActionBar(toolbar)

        showDeleteMenuIcon.value = false

        setupAdapter()
        setupSavedMoviesList()
        openMovieDetailsScreen()
        //getItemPositionAndId()
        receiveSelectedItemsFromAdapter()
    }

    private fun setupSavedMoviesList() {

        viewModel.savedMoviesList.observe(viewLifecycleOwner, {

            text_error.visibility = View.GONE
            progress_bar.visibility = View.GONE
            rv_saved_movies.visibility = View.GONE
            button_retry.visibility = View.GONE

            when (it) {
                ResponseResult.Loading -> {
                    progress_bar.visibility = View.VISIBLE
                }
                is ResponseResult.Failure -> {
                    button_retry.visibility = View.GONE
                    progress_bar.visibility = View.GONE
                    text_error.visibility = View.VISIBLE
                    text_error.text = it.message
                }
                is ResponseResult.Success -> {
                    progress_bar.visibility = View.GONE
                    rv_saved_movies.visibility = View.VISIBLE
                    savedMoviesAdapter.setupList(it.data)
                }
            }
        })


    }

    private fun setupAdapter() {
        savedMoviesAdapter = SavedMovieAdapter()
        rv_saved_movies.run {
            adapter = savedMoviesAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }


    private fun openMovieDetailsScreen() {
        savedMoviesAdapter.onclick(object : OnClickAdapterPopularMovieListener {
            override fun getPopularMovie(movie: PopularMovieWithDetailsModel) {
                val bundle = Bundle()
                bundle.putKSerializable("movieObject", movie)
                bundle.putBoolean("showSavedIcon", false)
                findNavController().navigate(
                    R.id.action_saved_movies_to_movieDetailsFragment,
                    bundle
                )
            }
        })

    }

//    private fun getItemPositionAndId() {
//        popularMovieId.clear()
//
//        savedMoviesAdapter.onLongClick(object : OnLongClickAdapterItme {
//            override fun getMoviePositionAndId(adapterPosition: Int, movieId: List<Int>) {
//                println("mLog: size = ${movieId.size}")
//                movieAdapterPosition = adapterPosition
//                popularMovieId.addAll(movieId)
//                showDeleteMenuIcon.value = true
//            }
//
//        })
//    }

    private fun receiveSelectedItemsFromAdapter() {
        savedMoviesAdapter.selectedElementsId.observe(viewLifecycleOwner){
            if(it.isEmpty()){
                showDeleteMenuIcon.value = false
            } else {
                showDeleteMenuIcon.value = true
                popularMovieId.clear()
                popularMovieId.addAll(it)
            }
        }
    }

    private fun deleteMovieFromList() {
        lifecycleScope.launch {
            viewModel.deleteMovieByIdFromDb(popularMovieId)
            savedMoviesAdapter.clearSelectedElementsList()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.saved_movies_toolbar_menu, menu)
        showDeleteMenuIcon.observe(viewLifecycleOwner,{
            val item: MenuItem = menu.findItem(R.id.delete_movie)
            item.isVisible = it
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_movie -> {
                deleteMovieFromList()
                showDeleteMenuIcon.value = false
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }
}