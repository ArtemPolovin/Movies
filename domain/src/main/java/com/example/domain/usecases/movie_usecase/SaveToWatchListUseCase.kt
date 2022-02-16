package com.example.domain.usecases.movie_usecase

import com.example.domain.models.SaveToWatchListModel
import com.example.domain.repositories.MoviesRepository

class SaveToWatchListUseCase(private val movieRepository: MoviesRepository) {
    suspend fun execute(saveToWatchListModel: SaveToWatchListModel, sessionId: String) =
        movieRepository.saveToWatchList(saveToWatchListModel, sessionId)
}