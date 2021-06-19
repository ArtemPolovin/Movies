package com.example.movies.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.models.PopularMovieModel
import com.example.domain.usecases.GetPopularMoviesUseCase
import com.example.movies.utils.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase
) : ViewModel() {

    private var disposeBag = CompositeDisposable()

    private val _popularMovies = MutableLiveData<ViewState<List<PopularMovieModel>>>()
    val popularMovies: LiveData<ViewState<List<PopularMovieModel>>> get() = _popularMovies

    private fun fetchPopularMovies() {

        _popularMovies.value = ViewState.Loading

        val single = getPopularMoviesUseCase()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _popularMovies.value = ViewState.Success(it)
                },
                {
                    _popularMovies.value = ViewState.Failure(message = "An error occured")
                    Log.i("mLog", "error = ${it.printStackTrace()}")
                }
            )
        disposeBag.add(single)
    }

    fun refreshMoviesList() {
        fetchPopularMovies()
    }



    override fun onCleared() {
        super.onCleared()
        disposeBag.clear()
    }

}