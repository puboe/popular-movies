package com.puboe.kotlin.moviedb.popularshows.entities

data class PopularTvShows(
    val page: Int,
    val totalResults: Int,
    val totalPages: Int,
    val shows: List<TvShow>
)