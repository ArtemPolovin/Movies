package com.example.data.repositories_impl

import com.example.data.apimodels.movie_details.MovieDetailsModelApi
import com.example.data.apimodels.movies.Result
import com.example.data.mapers.MoviesApiMapper
import com.example.data.network.MoviesApi
import com.example.domain.models.PopularMovieWithDetailsModel
import com.example.domain.repositories.MoviesRepository
import io.reactivex.Scheduler
import io.reactivex.Single

class MoviesRepositoryImpl(
    private val schedulersIO: Scheduler,
    private val moviesApi: MoviesApi,
    private val moviesApiMapper: MoviesApiMapper
) : MoviesRepository {


    private fun getPopularMovies(): Single<List<Result>> {
        return moviesApi.getPopularMovies()
            .map { it.results }

    }

    private fun getMovieDetailsList(): Single<List<MovieDetailsModelApi>> {

        return moviesApi.getPopularMovies().toObservable()
            .flatMapIterable {
                it.results
            }
            .flatMap {
                moviesApi.getMoviesDetails(it.id).toObservable()

            }
            .toList()
    }

   override fun getMoviesWithDetails() :Single<List<PopularMovieWithDetailsModel>>{
        return Single.zip(getPopularMovies(),getMovieDetailsList()) { movieList, detailsList ->
            moviesApiMapper.mapMovieDetailsAndMoviesToModelList(detailsList, movieList)
        }
            .subscribeOn(schedulersIO)

    }

}