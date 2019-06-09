package com.puboe.kotlin.moviedb.popularshows.entities

import com.puboe.kotlin.moviedb.popularshows.network.TvShowsParams

interface TvShowsRepository {

    suspend fun getPopularTvShows(params: TvShowsParams): PopularTvShows
}