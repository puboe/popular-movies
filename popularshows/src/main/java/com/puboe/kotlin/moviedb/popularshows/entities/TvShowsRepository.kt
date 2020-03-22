package com.puboe.kotlin.moviedb.popularshows.entities

import com.puboe.kotlin.moviedb.core.entities.DataResult
import com.puboe.kotlin.moviedb.popularshows.data.LocalCache
import kotlinx.coroutines.CoroutineScope

interface TvShowsRepository {

    fun getPopularTvShows(cache: LocalCache, coroutineScope: CoroutineScope): ShowResult

    suspend fun getNextPage(): DataResult<List<TvShow>>
}