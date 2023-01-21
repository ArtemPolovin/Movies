package com.sacramento.domain.usecases.movie_usecase.guest_session

import com.sacramento.domain.repositories.GuestSessionDbRepository

class DeleteListOfGuestWatchListUseCase(private val guestRepo: GuestSessionDbRepository) {
    suspend fun execute(movieIdList: List<Int>) = guestRepo.deleteListOfGuestWatchList(movieIdList)
}