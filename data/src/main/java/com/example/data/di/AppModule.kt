package com.example.data.di

import com.example.data.mapers.MoviesApiMapper
import com.example.data.network.MoviesApi
import com.example.data.repositories_impl.MoviesRepositoryImpl
import com.example.domain.repositories.MoviesRepository
import com.example.domain.usecases.GetPopularMoviesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMoviesRepository(
        @Named("io")
        schedulersIO: Scheduler,
        moviesApi: MoviesApi,
        moviesApiMapper: MoviesApiMapper
    ):MoviesRepository{
      return  MoviesRepositoryImpl(schedulersIO,moviesApi,moviesApiMapper)
    }

    @Provides
    @Named("io")
    fun provideSchedulersIO(): Scheduler = Schedulers.io()

    @Provides
    @Singleton
    fun provideMoviesApi(): MoviesApi = MoviesApi()

    @Provides
    fun provideMoviesApiMapper() = MoviesApiMapper()

    @Provides
    fun provideGetPopularMoviesUseCase(moviesRepository: MoviesRepository) = GetPopularMoviesUseCase(moviesRepository)

}