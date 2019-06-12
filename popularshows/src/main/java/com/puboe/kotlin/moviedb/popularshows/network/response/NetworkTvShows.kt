package com.puboe.kotlin.moviedb.popularshows.network.response

import com.google.gson.annotations.SerializedName

data class NetworkTvShows(
    @SerializedName("page") val page: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("results") val results: List<NetworkTvShow>
)
