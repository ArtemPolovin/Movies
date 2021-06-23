package com.example.data.di

import com.example.data.datasource.MoviePagingSource
import com.example.data.mapers.MoviesApiMapper
import com.example.data.network.MoviesApi
import com.example.data.repositories_impl.MoviesRepositoryImpl
import com.example.domain.repositories.MoviesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMoviesRepository(
        moviesApi: MoviesApi,
        moviesApiMapper: MoviesApiMapper
    ): MoviesRepository = MoviesRepositoryImpl(moviesApi,moviesApiMapper)

    @Provides
    fun provideMoviePagingSource(movieRepository: MoviesRepository) = MoviePagingSource(movieRepository)

    @Provides
    @Singleton
    fun provideMoviesApi(): MoviesApi = MoviesApi()

    @Provides
    fun provideMoviesApiMapper() = MoviesApiMapper()



}