package com.puboe.kotlin.moviedb.popularshows.network.response

data class NetworkTvShows(
    val page: Int,
    val totalResults: Int,
    val totalPages: Int,
    val results: List<NetworkTvShow>
)
