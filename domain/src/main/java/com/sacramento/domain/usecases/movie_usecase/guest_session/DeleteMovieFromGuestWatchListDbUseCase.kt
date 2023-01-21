package com.sacramento.domain.usecases.movie_usecase.guest_session

import com.sacramento.domain.repositories.GuestSessionDbRepository

class DeleteMovieFromGuestWatchListDbUseCase(private val guestRepo: GuestSessionDbRepository) {
    suspend fun execute(movieId: Int) = guestRepo.deleteGuestMovie(movieId)
}