package com.puboe.kotlin.moviedb.popularshows.entities

import com.puboe.kotlin.moviedb.core.entities.DataResult

interface TvShowsRepository {

    suspend fun getPopularTvShows(): DataResult<List<TvShow>>

    suspend fun getNextPage(): DataResult<List<TvShow>>
}