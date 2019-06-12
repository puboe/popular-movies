package com.puboe.kotlin.moviedb.popularshows.mocks.network

import com.puboe.kotlin.moviedb.core.entities.DataResult
import com.puboe.kotlin.moviedb.popularshows.entities.TvShow
import com.puboe.kotlin.moviedb.popularshows.entities.TvShowsRepository

class MovieDBFakeRepository : TvShowsRepository {

    private var errorType: String? = null
    private var page = 0

    override suspend fun getPopularTvShows(): DataResult<List<TvShow>> {
        page = 0
        return handleRequest()
    }

    override suspend fun getNextPage(): DataResult<List<TvShow>> = handleRequest()

    private fun handleRequest(): DataResult<List<TvShow>> {
        if (!errorType.isNullOrEmpty()) {
            val error = getErrorByType(errorType)
            errorType = null
            return error
        }
        return DataResult.Success(mockPage())
    }

    fun setReturnError(type: String) {
        errorType = type
    }

    fun reset() {
        errorType = null
        page = 0
    }

    private fun getErrorByType(errorType: String?): DataResult.Error {
        return if ("server" == errorType) {
            DataResult.Error.ServerError
        } else if ("client" == errorType) {
            DataResult.Error.ClientError
        } else {
            DataResult.Error.NetworkError
        }
    }

    private fun mockPage(): List<TvShow> {
        val list = (0 until (page + 1) * PAGE_SIZE).map {
            TvShow("24 season $it", 10f, "Just too many seasons", null)
        }
        page++
        return list
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}