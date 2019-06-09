package com.puboe.kotlin.moviedb.popularshows.network

import com.puboe.kotlin.moviedb.popularshows.network.response.NetworkTvShows
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface TvShowsService {

    @GET("tv/popular")
    fun getPopularTvShows(@Query("page") page: Int): Deferred<NetworkTvShows>
}