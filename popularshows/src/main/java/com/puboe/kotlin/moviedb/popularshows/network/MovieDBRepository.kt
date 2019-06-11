package com.puboe.kotlin.moviedb.popularshows.network

import com.puboe.kotlin.moviedb.core.entities.DataResult
import com.puboe.kotlin.moviedb.core.provider.DataProvider
import com.puboe.kotlin.moviedb.popularshows.entities.PopularTvShows
import com.puboe.kotlin.moviedb.popularshows.entities.TvShow
import com.puboe.kotlin.moviedb.popularshows.entities.TvShowsRepository
import javax.inject.Inject

class MovieDBRepository @Inject constructor(
    private val networkProvider: DataProvider<Int, DataResult<PopularTvShows>>
) : TvShowsRepository {

    private var currentPage = 0
    private var totalPages = Int.MAX_VALUE
    private var isRequestInProgress = false
    private var shows: MutableList<TvShow> = ArrayList()

    override suspend fun getPopularTvShows(): DataResult<List<TvShow>> {
        return requestPage(1, true)
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

    private fun handleResult(result: DataResult<PopularTvShows>, replaceResults: Boolean): DataResult<List<TvShow>> {
        return when (result) {
            is DataResult.Success -> handleSuccess(result.data, replaceResults)
            is DataResult.Error -> handleError(result)
            is DataResult.Ignore -> result
        }
    }

    private fun handleSuccess(result: PopularTvShows, replaceResults: Boolean): DataResult<List<TvShow>> {
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