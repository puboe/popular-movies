package com.puboe.kotlin.moviedb.popularshows.network

import com.puboe.kotlin.moviedb.core.entities.DataResult
import com.puboe.kotlin.moviedb.core.provider.DataMapper
import com.puboe.kotlin.moviedb.core.provider.DataProvider
import com.puboe.kotlin.moviedb.popularshows.entities.PopularTvShows
import com.puboe.kotlin.moviedb.popularshows.network.response.NetworkTvShows
import retrofit2.HttpException
import javax.inject.Inject

class MovieDBPopularTvShowsNetworkProvider @Inject constructor(
    private val mapper: DataMapper<NetworkTvShows, PopularTvShows>,
    private val service: TvShowsService
) : DataProvider<Int, DataResult<PopularTvShows>> {

    override suspend fun requestData(params: Int): DataResult<PopularTvShows> {
        try {
            // Retrofit provides main-safe suspend functions.
            val result = service.getPopularTvShows(params)
            return DataResult.Success(mapper.map(result))
        } catch (e: HttpException) {
            return when (e.code()) {
                in 400 until 500 -> DataResult.Error.ClientError
                else -> DataResult.Error.ServerError
            }
        } catch (e: Throwable) {
            return DataResult.Error.NetworkError
        }
    }
}