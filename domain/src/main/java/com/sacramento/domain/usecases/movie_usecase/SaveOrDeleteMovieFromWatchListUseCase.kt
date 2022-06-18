package com.sacramento.domain.usecases.movie_usecase

import com.sacramento.domain.models.SaveToWatchListModel
import com.sacramento.domain.repositories.MoviesRepository

class SaveOrDeleteMovieFromWatchListUseCase(private val movieRepository: MoviesRepository) {
    suspend fun execute(saveToWatchListModel: SaveToWatchListModel, sessionId: String) =
        movieRepository.saveToWatchList(saveToWatchListModel, sessionId)
}