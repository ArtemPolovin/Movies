package com.sacramento.movies.di

import com.sacramento.movies.notifications.TrendingMovieNotification
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface NotificationModule {

    fun provideTrendingMovieNotification() = TrendingMovieNotification()

}