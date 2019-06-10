package com.puboe.kotlin.moviedb.popularshows.di

import com.puboe.kotlin.moviedb.core.entities.DataResult
import com.puboe.kotlin.moviedb.core.provider.DataProvider
import com.puboe.kotlin.moviedb.popularshows.entities.PopularTvShows
import com.puboe.kotlin.moviedb.popularshows.entities.TvShowsRepository
import com.puboe.kotlin.moviedb.popularshows.network.MovieDBRepositoryImpl
import com.puboe.kotlin.moviedb.popularshows.network.TvShowsParams
import dagger.Module
import dagger.Provides

@Module
object EntitiesModule {

    @Provides
    @JvmStatic
    internal fun providesTvShowsRepository(
        dataProvider: @JvmSuppressWildcards DataProvider<TvShowsParams, DataResult<PopularTvShows>>
    ): TvShowsRepository {
        return MovieDBRepositoryImpl(dataProvider)
    }
}