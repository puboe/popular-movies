package com.puboe.kotlin.moviedb.popularshows.network

import com.puboe.kotlin.moviedb.core.entities.DataResult
import com.puboe.kotlin.moviedb.core.provider.DataProvider
import com.puboe.kotlin.moviedb.popularshows.entities.PopularTvShows
import com.puboe.kotlin.moviedb.popularshows.entities.TvShowsRepository
import javax.inject.Inject

class MovieDBRepositoryImpl @Inject constructor(
    private val networkProvider: DataProvider<TvShowsParams, DataResult<PopularTvShows>>
) : TvShowsRepository {

    override suspend fun getPopularTvShows(params: TvShowsParams): DataResult<PopularTvShows> {
        return networkProvider.requestData(params)
    }
}