package com.puboe.kotlin.moviedb.di

import com.puboe.kotlin.moviedb.MainApplication
import com.puboe.kotlin.moviedb.popularshows.di.PopularShowsModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        PopularShowsModule::class
    ]
)
interface AppComponent {

    fun inject(application: MainApplication)

    @Component.Builder
    interface Builder {

        fun build(): AppComponent

        @BindsInstance
        fun application(application: MainApplication): Builder
    }
}