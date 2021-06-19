package com.example.data.repositories_impl

import com.example.data.mapers.MoviesApiMapper
import com.example.data.network.MoviesApi
import com.example.domain.models.PopularMovieModel
import com.example.domain.repositories.MoviesRepository
import io.reactivex.Scheduler
import io.reactivex.Single

class MoviesRepositoryImpl(
    private val schedulersIO: Scheduler,
    private val moviesApi: MoviesApi,
    private val moviesApiMapper: MoviesApiMapper
): MoviesRepository {

    override fun getPopularMovies(): Single<List<PopularMovieModel>> {
        return moviesApi.getPopularMovies()
            .subscribeOn(schedulersIO)
            .map { moviesApiMapper.mapPopularMoviesApiToModelList(it) }
    }
}