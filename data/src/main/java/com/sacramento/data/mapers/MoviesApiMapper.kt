package com.sacramento.data.mapers

import com.sacramento.data.apimodels.movie_details.MovieDetailsModelApi
import com.sacramento.data.apimodels.movie_state.MovieAccountStateApiModel
import com.sacramento.data.apimodels.movies.MoviesListApiModel
import com.sacramento.data.apimodels.movies.Result
import com.sacramento.data.apimodels.trailers.TrailersApiModel
import com.sacramento.data.utils.POSTER_BASE_URL
import com.sacramento.domain.models.*

class MoviesApiMapper {

    fun mapMovieDetailsApiToModel(
        movieDetailsModelApi: MovieDetailsModelApi?,
    ): MovieWithDetailsModel? {

        if (movieDetailsModelApi != null) {
            return createMovieWithDetailsModel(movieDetailsModelApi)
        }
        return null
    }

    fun mapMovieDetailsApiToModel(
        movieDetailsModelApi: MovieDetailsModelApi?,
        genreList: List<GenreModel>,
        movieApiModel: Result,
    ): MovieWithDetailsModel {

        return if (movieDetailsModelApi != null) {
            return createMovieWithDetailsModel(movieDetailsModelApi)
        } else {
            MovieWithDetailsModel(
                releaseData = movieApiModel.release_date,
                popularityScore = movieApiModel.popularity.toString(),
                movieName = movieApiModel.title,
                rating = movieApiModel.vote_average.toFloat(),
                poster = "${POSTER_BASE_URL}${movieApiModel.poster_path}",
                backdropImage = "${POSTER_BASE_URL}${movieApiModel.backdrop_path}",
                overview = movieApiModel.overview,
                genres = getGenreNames(genreList, movieApiModel.genre_ids),
                homePageUrl = "",
                id = movieApiModel.id,
                voteCount = movieApiModel.vote_count.toString()
            )
        }
    }

    private fun createMovieWithDetailsModel(
        movieDetailsModelApi: MovieDetailsModelApi
    ): MovieWithDetailsModel {
        return MovieWithDetailsModel(
            releaseData = movieDetailsModelApi.release_date,
            popularityScore = movieDetailsModelApi.popularity.toString(),
            movieName = movieDetailsModelApi.title,
            rating = movieDetailsModelApi.vote_average?.toFloat(),
            poster = "${POSTER_BASE_URL}${movieDetailsModelApi.poster_path}",
            backdropImage = "${POSTER_BASE_URL}${movieDetailsModelApi.backdrop_path}",
            overview = movieDetailsModelApi.overview,
            genres = movieDetailsModelApi.genres?.joinToString(", ") { it.name ?: "" },
            homePageUrl = movieDetailsModelApi.homepage,
            id = movieDetailsModelApi.id,
            voteCount = movieDetailsModelApi.vote_count.toString()
        )

    }

    //This function takes list of MovieApiModle without movie details and maps it to list of MovieModel without movie details
    fun mapMovieApiToMovieModelList(movieApiModelsList: MoviesListApiModel?): List<MovieModel> {
        val moviesModelList = mutableListOf<MovieModel>()
        movieApiModelsList?.let { movieApiList ->
            movieApiList.results.forEach { result ->
                moviesModelList.add(
                    MovieModel(
                        movieId = result.id,
                        poster = "${POSTER_BASE_URL}${result.poster_path}",
                        title = result.title,
                        voteCount = result.vote_count,
                        rating = result.vote_average
                    )
                )
            }
        }
        return moviesModelList
    }

    // This function takes all list of GenreModels and list of genres id which are contained in the movie and
    // returns the names of the genres that the movie contains
    private fun getGenreNames(genresList: List<GenreModel>, genresIdList: List<Int>): String {
        return genresList.toMutableList().filter { genreModel ->
            genresIdList.any {
                it.toString() == genreModel.id
            }
        }.joinToString(", ") { it.name } ?: ""
    }

    fun mapMovieAccountStateApiToModel(movieAccountStateApiModel: MovieAccountStateApiModel) =
        MovieAccountStateModel(
            favorite = movieAccountStateApiModel.favorite,
            movieId = movieAccountStateApiModel.id,
            rated = movieAccountStateApiModel.rated,
            watchlist = movieAccountStateApiModel.watchlist
        )

    fun mapTrailerApiToModelsList(trailersApiModel: TrailersApiModel): List<TrailerModel> {
        return trailersApiModel.results.map {
            TrailerModel(
                thumbnailUrl = "https://img.youtube.com/vi/${it.key}/maxresdefault.jpg",
                videoKey = it.key,
                id = it.id
            )
        }
    }

    fun mapTrendingMoviesListToMovieModel(movieApiModelsList: MoviesListApiModel): MovieModel {
      val firstMovie = movieApiModelsList.results[0]
        return MovieModel(
            movieId = firstMovie.id,
            poster = "${POSTER_BASE_URL}${firstMovie.poster_path}",
            title = firstMovie.title,
            voteCount = firstMovie.vote_count,
            rating = firstMovie.vote_average
        )
    }


}
