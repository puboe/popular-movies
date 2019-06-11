package com.puboe.kotlin.moviedb.popularshows.util

import com.google.common.truth.Correspondence
import com.puboe.kotlin.moviedb.popularshows.entities.TvShow
import com.puboe.kotlin.moviedb.popularshows.network.response.NetworkTvShow

private val showMapper = fun(tvShow: TvShow?): NetworkTvShow? {
    return tvShow?.let {
        NetworkTvShow(it.name, it.rating, it.overview, it.poster)
    }
}

val showCorrespondence: Correspondence<TvShow, NetworkTvShow> =
    Correspondence.transforming(showMapper, "")

