package com.puboe.kotlin.moviedb.popularshows.network

import com.puboe.kotlin.moviedb.core.entities.DataResult
import com.puboe.kotlin.moviedb.core.provider.DataProvider
import com.puboe.kotlin.moviedb.popularshows.entities.PopularTvShows
import com.puboe.kotlin.moviedb.popularshows.entities.TvShowsRepository
import javax.inject.Inject

class MovieDBRepository @Inject constructor(
    private val networkProvider: DataProvider<Int, DataResult<PopularTvShows>>
) : TvShowsRepository {

    override suspend fun getPopularTvShows(params: Int): DataResult<PopularTvShows> {
        return networkProvider.requestData(params)
    }
}