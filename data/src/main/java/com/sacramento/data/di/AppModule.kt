package com.sacramento.data.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.google.gson.Gson
import com.sacramento.data.cache.*
import com.sacramento.data.datasource.*
import com.sacramento.data.db.AppDatabase
import com.sacramento.data.db.dao.GuestSessionDao
import com.sacramento.data.db.dao.MoviesDao
import com.sacramento.data.mapers.*
import com.sacramento.data.network.AuthMovieAPIService
import com.sacramento.data.network.GuestSessionApiService
import com.sacramento.data.network.MoviesApi
import com.sacramento.data.repositories_impl.*
import com.sacramento.data.utils.ConnectionHelper
import com.sacramento.data.utils.SHARED_PREF
import com.sacramento.data.utils.SHARED_PREF_MOVIE_FILTER
import com.sacramento.domain.repositories.*
import com.sacramento.domain.usecases.auth.*
import com.sacramento.domain.usecases.movie_usecase.*
import com.sacramento.domain.usecases.movie_usecase.guest_session.CheckIfGuestMovieIsSavedUseCase
import com.sacramento.domain.usecases.movie_usecase.guest_session.DeleteListOfGuestWatchListUseCase
import com.sacramento.domain.usecases.movie_usecase.guest_session.DeleteMovieFromGuestWatchListDbUseCase
import com.sacramento.domain.usecases.movie_usecase.guest_session.InsertGuestSavedMovieToDbUseCase
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
        settingsDataCache: SettingsDataCache,
        movieCategoriesRepository: MovieCategoriesRepository,
        movieDbRepository: MovieDBRepository
    ): MoviesRepository =
        MoviesRepositoryImpl(
            moviesApi,
            moviesApiMapper,
            settingsDataCache,
            movieCategoriesRepository,
            movieDbRepository
        )

    @Provides
    @Singleton
    fun provideMovieDBRepository(
        movieDao: MoviesDao,
        movieEntityMapper: MoviesEntityMapper,
        settingsDataCache: SettingsDataCache,
        movieCategoriesRepo: MovieCategoriesRepository
    ): MovieDBRepository =
        MovieDBRepositoryImpl(
            movieDao,
            movieEntityMapper,
            settingsDataCache,
            movieCategoriesRepo
        )

    @Provides
    @Singleton
    fun provideAuthMovieRepository(
        authMovieAPIService: AuthMovieAPIService,
        errorLoginMapper: ErrorLoginMapper,
        cacheRepository: CacheRepository
    ): AuthMovieRepository =
        AuthMovieRepositoryImpl(
            authMovieAPIService,
            errorLoginMapper,
            cacheRepository
        )

    @Provides
    @Singleton
    fun provideBackendLessRepositoryImpl(
        movieGenresMapper: MovieGenresMapper,
        settingsDataCache: SettingsDataCache,
        moviesApi: MoviesApi,
        moviesDao: MoviesDao
    ): MovieCategoriesRepository =
        MovieCategoriesRepositoryImpl(movieGenresMapper, settingsDataCache, moviesApi, moviesDao)

    @Provides
    @Singleton
    fun provideGuestSessionRepository(
        guestSessionApi: GuestSessionApiService,
        guestSessionMapper: GuestSessionMapper
    ): GuestSessionRepository =
        GuestSessionRepositoryImpl(guestSessionApi, guestSessionMapper)

    @Provides
    @Singleton
    fun provideCacheRepositoryImpl(
        sharedPref: SharedPreferences
    ): CacheRepository = CacheRepositoryImpl(sharedPref)

    @Provides
    @Singleton
    fun provideCookieRepositoryImpl(): CookieRepository = CookieRepositoryImpl()

    @Provides
    @Singleton
    fun provideGuestSessionDbRepositoryImpl(
        guestDao: GuestSessionDao,
        guestSessionMapper: GuestSessionMapper
    ): GuestSessionDbRepository = GuestSessionDbRepositoryImpl(guestDao, guestSessionMapper)


    @Provides
    fun provideMovieWithDetailsPagingSource(
        movieRepository: MoviesRepository
    ) = MoviesWithDetailsPagingSource(movieRepository)

    @Provides
    fun provideMovieWithDetailsPagingSourceDB(
        movieDBRepository: MovieDBRepository
    ) = MoviesWithDetailsPagingSourceDB(movieDBRepository)

    @Provides
    fun provideMoviesPagingSourceDB(movieDBRepository: MovieDBRepository) =
        MoviesPagingSourceDB(movieDBRepository)

    @Provides
    fun provideMoviesPagingSource(
        movieRepository: MoviesRepository,
        getWatchListUseCase: GetWatchListUseCase
    ) =
        MoviesPagingSource(movieRepository, getWatchListUseCase)

    @Provides
    fun provideGuestSessionPagingSource(guestRepo: GuestSessionDbRepository) =
        GuestSessionPagingSource(guestRepo)

    @Provides
    @Singleton
    fun provideMoviesApi(): MoviesApi = MoviesApi()

    @Provides
    @Singleton
    fun provideAuthMovieApiService(): AuthMovieAPIService = AuthMovieAPIService()

    @Provides
    @Singleton
    fun provideGuestSessionApiService(): GuestSessionApiService = GuestSessionApiService()

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, AppDatabase::class.java, "MovieDB")
            //.fallbackToDestructiveMigration()
            // .addMigrations(AppDatabase.MIGRATION_2_3)
            .build()

    @Provides
    @Singleton
    fun provideMoviesDao(appDatabase: AppDatabase) = appDatabase.getMoviesDao()

    @Provides
    @Singleton
    fun provideGuestSessionDao(appDatabase: AppDatabase) = appDatabase.getGuestSessionDao()

    @Provides
    fun provideMoviesApiMapper() = MoviesApiMapper()

    @Provides
    fun provideMovieEntityMapper(settingsDataCache: SettingsDataCache) =
        MoviesEntityMapper(settingsDataCache)

    @Provides
    fun provideErrorLoginMapper() = ErrorLoginMapper()

    @Provides
    fun provideMovieCategoriesMapper(
        gson: Gson,
        @ApplicationContext context: Context,
        settingsDataCache: SettingsDataCache
    ) = MovieGenresMapper(gson, context, settingsDataCache)

    @Provides
    fun provideGuestSessionMapper() = GuestSessionMapper()

    @Provides
    fun provideDeleteMovieByIdFromDbUseCase(movieDBRepository: MovieDBRepository) =
        DeleteMovieByIdFromDbUseCase(movieDBRepository)

    @Provides
    fun provideInsertMovieToDbUseCase(movieDBRepository: MovieDBRepository) =
        InsertMovieToDbUseCase(movieDBRepository)

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
    fun provideGetWatchListUseCase(
        movieRepository: MoviesRepository,
        loadSessionIdUseCase: LoadSessionIdUseCase,
        movieDBRepository: MovieDBRepository
    ) =
        GetWatchListUseCase(movieRepository, loadSessionIdUseCase, movieDBRepository)

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
    fun provideLoadSessionIdUseCase(cacheRepository: CacheRepository) =
        LoadSessionIdUseCase(cacheRepository)

    @Provides
    fun provideLoadRequestTokenUseCase(cacheRepository: CacheRepository) =
        LoadRequestTokenUseCase(cacheRepository)

    @Provides
    fun provideLogoutFromWebPageUseCase(
        cookieRepository: CookieRepository,
        cacheRepository: CacheRepository
    ) = LogoutFromWebPageUseCase(cookieRepository, cacheRepository)

    @Provides
    fun provideGetMovieByIdFromDBUseCase(movieDBRepository: MovieDBRepository) =
        GetMovieByIdFromDBUseCase(movieDBRepository)

    @Provides
    fun provideGetSimilarMoviesFromDBUseCase(movieDBRepository: MovieDBRepository) =
        GetSimilarMoviesFromDBUseCase(movieDBRepository)

    @Provides
    fun provideGetRecommendationsMoviesFromDBUseCase(movieDBRepository: MovieDBRepository) =
        GetRecommendationsMoviesFromDBUseCase(movieDBRepository)

    @Provides
    fun provideInsertAllSavedMoviesFromAccountToDBUseCase(movieDBRepository: MovieDBRepository) =
        InsertAllSavedMoviesFromAccountToDBUseCase(movieDBRepository)

    @Provides
    fun provideGetMoviesSortedByGenreFromDbUseCase(movieDBRepository: MovieDBRepository) =
        GetMoviesSortedByGenreFromDbUseCase(movieDBRepository)

    @Provides
    fun provideGetUpComingMoviesFromDbUseCase(movieDBRepository: MovieDBRepository) =
        GetUpComingMoviesFromDbUseCase(movieDBRepository)

    @Provides
    fun provideCleanSavedMoviesDbUseCase(movieDBRepository: MovieDBRepository) =
        CleanSavedMoviesDbUseCase(movieDBRepository)

    @Provides
    fun provideGetGuestSessionUseCase(guestSessionRepo: GuestSessionRepository) =
        GetGuestSessionUseCase(guestSessionRepo)

    @Provides
    fun provideInsertGuestSavedMovieToDbUseCase(guestSessionDbRepo: GuestSessionDbRepository) =
        InsertGuestSavedMovieToDbUseCase(guestSessionDbRepo)

    @Provides
    fun provideCheckIfGuestMovieIsSavedUseCase(guestSessionDbRepo: GuestSessionDbRepository) =
        CheckIfGuestMovieIsSavedUseCase(guestSessionDbRepo)

    @Provides
    fun provideDeleteMovieFromGuestWatchListDbUseCase(guestSessionDbRepo: GuestSessionDbRepository) =
        DeleteMovieFromGuestWatchListDbUseCase(guestSessionDbRepo)

    @Provides
    fun provideDeleteListOfGuestWatchListUseCase(guestSessionDbRepo: GuestSessionDbRepository) =
        DeleteListOfGuestWatchListUseCase(guestSessionDbRepo)

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
    fun provideGuestSessionSharedPref(sharedPref: SharedPreferences) =
        GuestSessionSharedPref(sharedPref)


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

    @Provides
    @Singleton
    fun provideConnectionHelper(@ApplicationContext context: Context) = ConnectionHelper(context)


}