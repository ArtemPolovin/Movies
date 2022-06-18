package com.sacramento.movies.di

import android.content.Context
import com.sacramento.movies.work_manager.TrendingMovieWorker
import dagger.BindsInstance
import dagger.Component

@Component(dependencies = [NotificationModule::class])
interface NotificationComponent {

    fun inject (trendingMovieWorker: TrendingMovieWorker)

    @Component.Builder
    interface Builder{
        fun context(@BindsInstance context: Context): Builder
        fun appDependencies(notificationModule: NotificationModule): Builder
        fun build(): NotificationComponent
    }
}