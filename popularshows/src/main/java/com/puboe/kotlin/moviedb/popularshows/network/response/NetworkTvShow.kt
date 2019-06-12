package com.puboe.kotlin.moviedb.popularshows.network.response

import com.google.gson.annotations.SerializedName

data class NetworkTvShow(
    @SerializedName("name") val name: String,
    @SerializedName("vote_average") val voteAverage: Float,
    @SerializedName("overview") val overview: String,
    @SerializedName("poster_path") val posterPath: String?
)
