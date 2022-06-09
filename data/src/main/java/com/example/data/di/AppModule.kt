package com.example.data.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.example.data.cache.*
import com.example.data.datasource.MoviesPagingSource
import com.example.data.datasource.MoviesWithDetailsPagingSource
import com.example.data.db.AppDatabase
import com.example.data.db.dao.MoviesDao
import com.example.data.mapers.ErrorLoginMapper
import com.example.data.mapers.MovieGenresMapper
import com.example.data.mapers.MoviesApiMapper
import com.example.data.mapers.MoviesEntityMapper
import com.example.data.network.AuthMovieAPIService
import com.example.data.network.MoviesApi
import com.example.data.repositories_impl.*
import com.example.data.utils.SHARED_PREF
import com.example.data.utils.SHARED_PREF_MOVIE_FILTER
import com.example.domain.repositories.*
import com.example.domain.usecases.auth.LoginUseCase
import com.example.domain.usecases.auth.LogoutUseCase
import com.example.domain.usecases.auth.SaveRequestTokenUseCase
import com.example.domain.usecases.auth.SaveSessionIdUseCase
import com.example.domain.usecases.movie_usecase.*
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
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
        movieEntityMapper: MoviesEntityMapper,
        settingsDataCache: SettingsDataCache,
        movieCategoriesRepository: MovieCategoriesRepository
    ): MoviesRepository =
        MoviesRepositoryImpl(
            moviesApi,
            moviesApiMapper,
            movieDao,
            movieEntityMapper,
            settingsDataCache,
            movieCategoriesRepository
        )

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
    @Singleton
    fun provideBackendLessRepositoryImpl(
        movieGenresMapper: MovieGenresMapper,
        settingsDataCache: SettingsDataCache,
        moviesApi: MoviesApi
    ): MovieCategoriesRepository =
        MovieCategoriesRepositoryImpl(movieGenresMapper, settingsDataCache, moviesApi)

    @Provides
    @Singleton
    fun provideCacheRepositoryImpl(
        sessionIdDataCache: SessionIdDataCache,
        requestTokenDataCache: RequestTokenDataCache
    ): CacheRepository = CacheRepositoryImpl(sessionIdDataCache, requestTokenDataCache)

    @Provides
    @Singleton
    fun provideCookieRepositoryImpl(): CookieRepository = CookieRepositoryImpl()


    @Provides
    fun provideMovieWithDetailsPagingSource(
        movieRepository: MoviesRepository,
        sharedPrefMovieCategory: SharedPrefMovieCategory,
        shardPrefMovieFilter: SharedPrefMovieFilter
    ) = MoviesWithDetailsPagingSource(
        movieRepository,
        sharedPrefMovieCategory,
        shardPrefMovieFilter
    )

    @Provides
    fun provideMoviesPagingSource(movieRepository: MoviesRepository) =
        MoviesPagingSource(movieRepository)

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
            // .fallbackToDestructiveMigration()
            // .addMigrations(AppDatabase.MIGRATION_2_3)
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
    fun provideMovieCategoriesMapper(
        gson: Gson,
        @ApplicationContext context: Context
    ) = MovieGenresMapper(gson, context)

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
    fun provideLogoutUseCase(
        authMovieRepository: AuthMovieRepository,
        cookieRepository: CookieRepository,
        cacheRepository: CacheRepository
    ) = LogoutUseCase(authMovieRepository, cookieRepository, cacheRepository)

    @Provides
    fun provideGetGenresUseCase(movieCategoriesRepo: MovieCategoriesRepository) =
        GetGenresUseCase(movieCategoriesRepo)

    @Provides
    fun provideGetMoviesSortedByGenreUseCase(moviesRepo: MoviesRepository) =
        GetMoviesSortedByGenreUseCase(moviesRepo)

    @Provides
    fun provideGetMovieDetailsUseCase(moviesRepo: MoviesRepository) =
        GetMovieDetailsUseCase(moviesRepo)

    @Provides
    fun provideGetUpcomingMoviesUseCase(moviesRepository: MoviesRepository) =
        GetUpComingMoviesUseCase(moviesRepository)

    @Provides
    fun provideGetSimilarMoviesUseCase(moviesRepository: MoviesRepository) =
        GetSimilarMoviesUseCase(moviesRepository)

    @Provides
    fun provideGetRecommendationsMoviesUseCase(moviesRepository: MoviesRepository) =
        GetRecommendationsMoviesUseCase(moviesRepository)

    @Provides
    fun provideGetMoviePosterUseCase(moviesRepository: MoviesRepository) =
        GetMoviePosterUseCase(moviesRepository)


    @Provides
    fun provideSaveToWatchListUseCase(movieRepository: MoviesRepository) =
        SaveOrDeleteMovieFromWatchListUseCase(movieRepository)

    @Provides
    fun provideGetWatchListUseCase(movieRepository: MoviesRepository) =
        GetWatchListUseCase(movieRepository)

    @Provides
    fun provideGetMovieAccountStateUseCase(movieRepository: MoviesRepository) =
        GetMovieAccountStateUseCase(movieRepository)

    @Provides
    fun provideGetTrailerListUseCase(movieRepository: MoviesRepository) =
        GetTrailerListUseCase(movieRepository)

    @Provides
    fun provideGetMoviesCategoriesCells(movieCategoriesRepo: MovieCategoriesRepository) =
        GetMoviesCategoriesUseCase(movieCategoriesRepo)

    @Provides
    fun provideGetTrendingMovieUseCase(movieRepository: MoviesRepository) =
        GetTrendingMovieUseCase(movieRepository)

    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences =
        context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    @Named("MovieFilterCache")
    fun provideMovieFilterSharedPreferences(@ApplicationContext context: Context) =
        context.getSharedPreferences(SHARED_PREF_MOVIE_FILTER, Context.MODE_PRIVATE)


    @Provides
    @Singleton
    fun provideRequestTokenDataCache(sharedPref: SharedPreferences) =
        RequestTokenDataCache(sharedPref)

    @Provides
    @Singleton
    fun provideSessionIdDataCache(sharedPref: SharedPreferences) = SessionIdDataCache(sharedPref)

    @Provides
    @Singleton
    fun provideSettingsDataCache(@Named("SettingsPrefManager") sharedPrefManger: SharedPreferences) =
        SettingsDataCache(sharedPrefManger)

    @Provides
    @Singleton
    fun provideSharedPreferencesLogin(sharedPref: SharedPreferences) =
        SharedPreferencesLoginRememberMe(sharedPref)

    @Provides
    @Singleton
    fun provideSharedPrefLoginAndPassword(sharedPref: SharedPreferences) =
        SharedPrefLoginAndPassword(sharedPref)

    @Provides
    @Singleton
    fun provideSharedPrefMovieCategory(sharedPref: SharedPreferences) =
        SharedPrefMovieCategory(sharedPref)

    @Provides
    @Singleton
    fun provideWatchListChanges(sharedPref: SharedPreferences) = WatchListChanges(sharedPref)

    @Provides
    @Singleton
    fun provideSharedPrefTrendingMovieId(sharedPref: SharedPreferences) =
        SharedPrefTrendingMovieId(sharedPref)


    @Provides
    @Singleton
    fun provideSharedPrefMovieFilter(@Named("MovieFilterCache") movieFilterSharedPref: SharedPreferences) =
        SharedPrefMovieFilter(movieFilterSharedPref)

    @Provides
    @Singleton
    @Named("SettingsPrefManager")
    fun providePreferenceManager(@ApplicationContext context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context.applicationContext)

    @Provides
    fun provideGson() = Gson()

}