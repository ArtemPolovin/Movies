package com.sacramento.domain.usecases.movie_usecase

import com.sacramento.domain.repositories.GuestSessionRepository

class GetGuestSessionUseCase(private val guestSessionRepository: GuestSessionRepository) {
    suspend fun invoke() = guestSessionRepository.getGuestSession()
}