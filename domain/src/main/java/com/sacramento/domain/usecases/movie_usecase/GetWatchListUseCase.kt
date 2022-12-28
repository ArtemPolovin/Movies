package com.sacramento.domain.usecases.movie_usecase

import com.sacramento.domain.models.MovieModel
import com.sacramento.domain.repositories.MovieDBRepository
import com.sacramento.domain.repositories.MoviesRepository
import com.sacramento.domain.usecases.auth.LoadSessionIdUseCase
import kotlinx.coroutines.delay

class GetWatchListUseCase(
    private val moviesRepository: MoviesRepository,
    private val loadSessionIdUseCase: LoadSessionIdUseCase,
    private val movieDBRepository: MovieDBRepository
) {

    suspend fun execute(page: Int): List<MovieModel> {
       val savedMovies =  moviesRepository.getWatchList(
            sessionId = loadSessionIdUseCase.execute(),
            page = page
        )
        insertSavedMoviesToDB(savedMovies)
        return savedMovies
    }

    private suspend fun insertSavedMoviesToDB(savedMoviesList: List<MovieModel>) {
        if(savedMoviesList.isNotEmpty())
        movieDBRepository.insertAllSavedMoviesFromAccount(savedMoviesList)
    }
}