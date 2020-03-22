package com.puboe.kotlin.moviedb.popularshows.network

import com.puboe.kotlin.moviedb.core.provider.DataMapper
import com.puboe.kotlin.moviedb.popularshows.entities.PopularTvShowsPage
import com.puboe.kotlin.moviedb.popularshows.entities.TvShow
import com.puboe.kotlin.moviedb.popularshows.network.response.NetworkTvShow
import com.puboe.kotlin.moviedb.popularshows.network.response.NetworkTvShows

class MovieDBTvShowsMapper : DataMapper<NetworkTvShows, PopularTvShowsPage> {

    override fun map(source: NetworkTvShows): PopularTvShowsPage {
        val shows = source.results.mapIndexed { index: Int, it: NetworkTvShow ->
            TvShow(it.id, it.name, it.voteAverage, it.overview, it.posterPath, (source.page - 1) * 20 + index)
        }
        return PopularTvShowsPage(source.page, source.totalPages, shows)
    }
}