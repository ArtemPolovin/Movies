package com.example.data.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.data.datasource.MoviePagingSource
import com.example.data.db.AppDatabase
import com.example.data.db.dao.MoviesDao
import com.example.data.mapers.ErrorLoginMapper
import com.example.data.mapers.MoviesApiMapper
import com.example.data.mapers.MoviesEntityMapper
import com.example.data.network.AuthMovieAPIService
import com.example.data.network.MoviesApi
import com.example.data.repositories_impl.AuthMovieRepositoryImpl
import com.example.data.repositories_impl.MoviesRepositoryImpl
import com.example.data.utils.RequestTokenDataCache
import com.example.data.utils.SHARED_PREF
import com.example.data.utils.SessionIdDataCache
import com.example.domain.repositories.AuthMovieRepository
import com.example.domain.repositories.MoviesRepository
import com.example.domain.usecases.auth.LoginUseCase
import com.example.domain.usecases.auth.LogoutUseCase
import com.example.domain.usecases.auth.SaveRequestTokenUseCase
import com.example.domain.usecases.auth.SaveSessionIdUseCase
import com.example.domain.usecases.movie_usecase.DeleteMovieByIdFromDbUseCase
import com.example.domain.usecases.movie_usecase.GetAllSavedMoviesFromDbUseCase
import com.example.domain.usecases.movie_usecase.InsertMovieToDbUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMoviesRepository(
        moviesApi: MoviesApi,
        moviesApiMapper: MoviesApiMapper,
        movieDao: MoviesDao,
        movieEntityMapper: MoviesEntityMapper
    ): MoviesRepository =
        MoviesRepositoryImpl(moviesApi, moviesApiMapper, movieDao, movieEntityMapper)

    @Provides
    @Singleton
    fun provideAuthMovieRepository(
        authMovieAPIService: AuthMovieAPIService,
        requestTokenDataCache: RequestTokenDataCache,
        errorLoginMapper: ErrorLoginMapper,
        sessionIdDataCache: SessionIdDataCache
    ): AuthMovieRepository =
        AuthMovieRepositoryImpl(
            authMovieAPIService,
            requestTokenDataCache,
            errorLoginMapper,
            sessionIdDataCache
        )

    @Provides
    fun provideMoviePagingSource(movieRepository: MoviesRepository) =
        MoviePagingSource(movieRepository)

    @Provides
    @Singleton
    fun provideMoviesApi(): MoviesApi = MoviesApi()

    @Provides
    @Singleton
    fun provideAuthMovieApiService(): AuthMovieAPIService = AuthMovieAPIService()

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, AppDatabase::class.java, "MovieDB")
            //  .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideMoviesDao(appDatabase: AppDatabase) = appDatabase.getMoviesDao()

    @Provides
    fun provideMoviesApiMapper() = MoviesApiMapper()

    @Provides
    fun provideMovieEntityMapper() = MoviesEntityMapper()

    @Provides
    fun provideErrorLoginMapper() = ErrorLoginMapper()

    @Provides
    fun provideDeleteMovieByIdFromDbUseCase(movieRepository: MoviesRepository) =
        DeleteMovieByIdFromDbUseCase(movieRepository)

    @Provides
    fun provideGetAllMoviesFromDbUseCase(movieRepository: MoviesRepository) =
        GetAllSavedMoviesFromDbUseCase(movieRepository)

    @Provides
    fun provideInsertMovieToDbUseCase(movieRepository: MoviesRepository) =
        InsertMovieToDbUseCase(movieRepository)

    @Provides
    fun provideLoginUseCase(authMovieRepository: AuthMovieRepository) =
        LoginUseCase(authMovieRepository)

    @Provides
    fun provideSaveRequestTokenUseCase(authMovieRepository: AuthMovieRepository) =
        SaveRequestTokenUseCase(authMovieRepository)

    @Provides
    fun provideSaveSessionIdUseCase(authMovieRepository: AuthMovieRepository) =
        SaveSessionIdUseCase(authMovieRepository)

    @Provides
    fun provideLogoutUseCase(authMovieRepository: AuthMovieRepository) = LogoutUseCase(authMovieRepository)

    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences =
        context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideRequestTokenDataCache(sharedPref: SharedPreferences) =
        RequestTokenDataCache(sharedPref)

    @Provides
    @Singleton
    fun provideSessionIdDataCache(sharedPref: SharedPreferences) = SessionIdDataCache(sharedPref)

//    @Provides
//    @Singleton
//    fun provideSharedPreferencesLogin() = SharedPreferencesLogin()


}