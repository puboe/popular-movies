package com.puboe.kotlin.moviedb.popularshows.di

import com.puboe.kotlin.moviedb.popularshows.view.PopularTvShowsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(
    includes = [
        NetworkModule::class,
        EntitiesModule::class,
        ViewModelModule::class
    ]
)
abstract class PopularShowsModule {

    @ContributesAndroidInjector
    abstract fun bindPopularTvShowsActivity(): PopularTvShowsActivity
}