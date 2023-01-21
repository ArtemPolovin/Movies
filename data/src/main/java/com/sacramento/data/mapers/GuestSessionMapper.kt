package com.sacramento.data.mapers

import com.sacramento.data.apimodels.guest_session.GuestSessionApiModel
import com.sacramento.data.db.tables.guest_session.GuestSavedMoviesEntity
import com.sacramento.domain.models.GuestSessionModel
import com.sacramento.domain.models.MovieModel
import com.sacramento.domain.models.MovieWithDetailsModel

class GuestSessionMapper {

    fun mapGuestSessionApiToModel(guestSessionApi: GuestSessionApiModel): GuestSessionModel {
        return GuestSessionModel(
            expireAt = guestSessionApi.expires_at,
            guestSessionId = guestSessionApi.guest_session_id,
            session = guestSessionApi.success
        )
    }

    fun mapMovieModelToEntity(movieModel: MovieWithDetailsModel): GuestSavedMoviesEntity {
        return GuestSavedMoviesEntity(
            movie_id = movieModel.id,
            poster = movieModel.poster,
            title = movieModel.movieName,
            rating = movieModel.rating?.toDouble(),
            vote_count = movieModel.voteCount?.toInt()
        )
    }

    fun mapGuestSavedMovieEntityToMovieModelList(guestEntityList: List<GuestSavedMoviesEntity>): List<MovieModel> {
       return guestEntityList.map {
            MovieModel(
                movieId = it.movie_id,
                poster = it.poster,
                title = it.title,
                rating = String.format("%.1f",it.rating ),
                voteCount = it.vote_count
            )
        }
    }
}