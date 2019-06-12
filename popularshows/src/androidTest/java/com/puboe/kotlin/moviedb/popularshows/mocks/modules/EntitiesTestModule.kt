package com.puboe.kotlin.moviedb.popularshows.mocks.modules

import com.puboe.kotlin.moviedb.popularshows.entities.TvShowsRepository
import dagger.Module
import dagger.Provides

@Module
class EntitiesTestModule(private val repository: TvShowsRepository) {

    @Provides
    fun provideTvShowsRepository(): TvShowsRepository {
        return repository
    }
}