package com.puboe.kotlin.moviedb.popularshows.network

import com.puboe.kotlin.moviedb.core.provider.DataProvider
import com.puboe.kotlin.moviedb.popularshows.entities.PopularTvShows
import com.puboe.kotlin.moviedb.popularshows.entities.TvShowsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieDBRepositoryImpl @Inject constructor(
    private val networkProvider: DataProvider<TvShowsParams, PopularTvShows>
) : TvShowsRepository {

    override suspend fun getPopularTvShows(params: TvShowsParams): PopularTvShows {
        return withContext(Dispatchers.IO) {
            networkProvider.requestData(params)
        }
    }
}