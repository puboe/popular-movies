package com.puboe.kotlin.moviedb.popularshows.mocks.modules

import com.puboe.kotlin.moviedb.popularshows.di.ViewModelModule
import com.puboe.kotlin.moviedb.popularshows.view.PopularTvShowsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [ViewModelModule::class])
abstract class PopularShowsTestModule {

    @ContributesAndroidInjector
    abstract fun bindPopularTvShowsActivity(): PopularTvShowsActivity
}