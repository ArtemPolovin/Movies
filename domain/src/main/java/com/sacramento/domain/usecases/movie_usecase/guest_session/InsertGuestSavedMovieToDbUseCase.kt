package com.sacramento.domain.usecases.movie_usecase.guest_session

import com.sacramento.domain.models.MovieModel
import com.sacramento.domain.models.MovieWithDetailsModel
import com.sacramento.domain.repositories.GuestSessionDbRepository

class InsertGuestSavedMovieToDbUseCase(private val guestSessionDbRepository: GuestSessionDbRepository) {
    suspend fun execute(movieModel: MovieWithDetailsModel) = guestSessionDbRepository.insertGuestMovie(movieModel)
}