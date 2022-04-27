package com.example.movies.di

import android.content.Context
import com.example.movies.notifications.TrendingMovieNotification
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface NotificationModule {

    fun provideTrendingMovieNotification() = TrendingMovieNotification()

}