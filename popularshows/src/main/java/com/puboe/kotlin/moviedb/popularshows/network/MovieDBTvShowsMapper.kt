package com.puboe.kotlin.moviedb.popularshows.network

import com.puboe.kotlin.moviedb.core.provider.DataMapper
import com.puboe.kotlin.moviedb.popularshows.entities.PopularTvShows
import com.puboe.kotlin.moviedb.popularshows.entities.TvShow
import com.puboe.kotlin.moviedb.popularshows.network.response.NetworkTvShows

class MovieDBTvShowsMapper : DataMapper<NetworkTvShows, PopularTvShows> {

    override fun map(source: NetworkTvShows): PopularTvShows {
        val shows = source.results.map {
            TvShow(it.name, it.voteAverage, it.overview, it.posterPath)
        }
        return PopularTvShows(source.page, source.totalPages, shows)
    }
}