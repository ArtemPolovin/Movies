package com.example.domain.usecases.movie_usecase

import com.example.domain.models.MoviePosterViewPagerModel
import com.example.domain.repositories.MoviesRepository
import retrofit2.HttpException
import java.io.IOException

class GetUpComingMoviesUseCase(private val moviesRepository: MoviesRepository) {
    suspend fun execute(page: Int): List<MoviePosterViewPagerModel>? {
      return  try {
           val moviesList =  moviesRepository.getUpcomingMoviesWithDetails(page)
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
          null
        } catch (e: HttpException) {
            e.printStackTrace()
          null
        } catch (e: IllegalArgumentException){
        e.printStackTrace()
          null
    }
}


}