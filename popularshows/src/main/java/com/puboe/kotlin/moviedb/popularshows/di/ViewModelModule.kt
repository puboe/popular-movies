package com.puboe.kotlin.moviedb.popularshows.di

import androidx.lifecycle.ViewModelProvider
import com.puboe.kotlin.moviedb.popularshows.entities.TvShowsRepository
import com.puboe.kotlin.moviedb.popularshows.view.PopularTvShowsViewModel
import dagger.Module
import dagger.Provides

@Module
object ViewModelModule {

    @Provides
    @JvmStatic
    internal fun providesViewModelFactory(repository: TvShowsRepository): ViewModelProvider.Factory {
        return PopularTvShowsViewModel.FACTORY(repository)
    }
}