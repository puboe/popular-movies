package com.puboe.kotlin.moviedb.popularshows.entities

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.puboe.kotlin.moviedb.core.entities.DataResult

data class ShowResult(
    val data: LiveData<PagedList<TvShow>>,
    val errors: LiveData<DataResult.Error>
)