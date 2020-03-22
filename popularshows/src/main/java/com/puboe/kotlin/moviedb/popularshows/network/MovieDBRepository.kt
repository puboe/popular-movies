package com.puboe.kotlin.moviedb.popularshows.network

import androidx.paging.LivePagedListBuilder
import com.puboe.kotlin.moviedb.core.entities.DataResult
import com.puboe.kotlin.moviedb.core.provider.DataProvider
import com.puboe.kotlin.moviedb.popularshows.data.BoundaryCallback
import com.puboe.kotlin.moviedb.popularshows.data.LocalCache
import com.puboe.kotlin.moviedb.popularshows.entities.PopularTvShowsPage
import com.puboe.kotlin.moviedb.popularshows.entities.ShowResult
import com.puboe.kotlin.moviedb.popularshows.entities.TvShow
import com.puboe.kotlin.moviedb.popularshows.entities.TvShowsRepository
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class MovieDBRepository @Inject constructor(
    // private val cache: LocalCache,
    private val networkProvider: DataProvider<Int, DataResult<PopularTvShowsPage>>
) : TvShowsRepository {

    private var currentPage = 0
    private var totalPages = Int.MAX_VALUE
    private var isRequestInProgress = false
    private var shows: MutableList<TvShow> = ArrayList()

    override fun getPopularTvShows(cache: LocalCache, coroutineScope: CoroutineScope): ShowResult {
        // Get data source factory from the local cache
        val dataSourceFactory = cache.getTvShows()

        // Construct the boundary callback.
        val boundaryCallback = BoundaryCallback(networkProvider, cache, coroutineScope)
        val networkErrors = boundaryCallback.errors

        // Get the paged list
        val data = LivePagedListBuilder(dataSourceFactory, 20)
            .setBoundaryCallback(boundaryCallback)
            .build()

        // Get the network errors exposed by the boundary callback
        return ShowResult(data, networkErrors)
    }

    override suspend fun getNextPage(): DataResult<List<TvShow>> {
        return requestPage(currentPage + 1, false)
    }

    private suspend fun requestPage(page: Int, replaceResults: Boolean): DataResult<List<TvShow>> {
        if (isRequestInProgress || page > totalPages) {
            return DataResult.Ignore
        }

        isRequestInProgress = true
        val result = networkProvider.requestData(page)

        return handleResult(result, replaceResults)
    }

    private fun handleResult(
        result: DataResult<PopularTvShowsPage>,
        replaceResults: Boolean
    ): DataResult<List<TvShow>> {
        return when (result) {
            is DataResult.Success -> handleSuccess(result.data, replaceResults)
            is DataResult.Error -> handleError(result)
            is DataResult.Ignore -> result
        }
    }

    private fun handleSuccess(result: PopularTvShowsPage, replaceResults: Boolean): DataResult<List<TvShow>> {
        currentPage = result.page
        totalPages = result.totalPages
        if (replaceResults) {
            shows = result.shows as MutableList<TvShow>
        } else {
            shows.addAll(result.shows)
        }
        isRequestInProgress = false
        return DataResult.Success(shows)
    }

    private fun handleError(result: DataResult.Error): DataResult.Error {
        isRequestInProgress = false
        return result
    }
}