package com.puboe.kotlin.moviedb.popularshows.network

import com.puboe.kotlin.moviedb.core.provider.DataMapper
import com.puboe.kotlin.moviedb.core.provider.DataProvider
import com.puboe.kotlin.moviedb.popularshows.entities.PopularTvShows
import com.puboe.kotlin.moviedb.popularshows.network.response.NetworkTvShows
import javax.inject.Inject

class MovieDBNetworkProvider @Inject constructor(
    private val mapper: DataMapper<NetworkTvShows, PopularTvShows>,
    private val service: TvShowsService
) : DataProvider<TvShowsParams, PopularTvShows> {

    override suspend fun requestData(params: TvShowsParams): PopularTvShows {
        // TODO: Add api key via interceptor.
        return mapper.map(service.getPopularTvShows(params.page, params.apiKey).await())
    }
}