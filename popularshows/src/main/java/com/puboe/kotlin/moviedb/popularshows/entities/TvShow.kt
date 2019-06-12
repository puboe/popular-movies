package com.puboe.kotlin.moviedb.popularshows.entities

data class TvShow(
    val name: String,
    val rating: Float,
    val overview: String,
    val poster: String?
)