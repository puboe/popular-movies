package com.puboe.kotlin.moviedb.popularshows.network.response

data class NetworkTvShow(
    val name: String,
    val voteAverage: Float,
    val overview: String,
    val posterPath: String?
)
