package com.sacramento.domain.usecases.movie_usecase

import com.sacramento.domain.models.MoviePosterViewPagerModel
import com.sacramento.domain.repositories.MovieDBRepository
import retrofit2.HttpException
import java.io.IOException

class GetUpComingMoviesFromDbUseCase(private val moviesDBRepository: MovieDBRepository) {
    suspend fun execute(): List<MoviePosterViewPagerModel> {
        return try {
            val moviesList = moviesDBRepository.getUpcomingMovies()
            moviesList.take(10).map {
                MoviePosterViewPagerModel(
                    poster = it.backdropImage,
                    movieName = it.movieName,
                    genreName = it.genres,
                    movieId = it.id
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
            emptyList<MoviePosterViewPagerModel>()
        } catch (e: HttpException) {
            e.printStackTrace()
            emptyList<MoviePosterViewPagerModel>()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            emptyList<MoviePosterViewPagerModel>()
        }
    }

}