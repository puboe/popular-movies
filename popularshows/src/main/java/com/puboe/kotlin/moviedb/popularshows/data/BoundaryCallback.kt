package com.puboe.kotlin.moviedb.popularshows.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.puboe.kotlin.moviedb.core.entities.DataResult
import com.puboe.kotlin.moviedb.core.provider.DataProvider
import com.puboe.kotlin.moviedb.popularshows.entities.PopularTvShowsPage
import com.puboe.kotlin.moviedb.popularshows.entities.TvShow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class BoundaryCallback(
    private val service: DataProvider<Int, DataResult<PopularTvShowsPage>>,
    private val cache: LocalCache,
    private val coroutineScope: CoroutineScope
) : PagedList.BoundaryCallback<TvShow>() {

    // keep the last requested page. When the request is successful, increment the page number.
    private var lastRequestedPage = 1
    private var totalPages = 1000

    private val _errors = MutableLiveData<DataResult.Error>()

    // LiveData of errors.
    val errors: LiveData<DataResult.Error>
        get() = _errors

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    override fun onZeroItemsLoaded() {
        requestAndSaveData()
    }

    override fun onItemAtEndLoaded(itemAtEnd: TvShow) {
        requestAndSaveData()
    }

    private fun requestAndSaveData() = coroutineScope.launch {
        if (isRequestInProgress) return@launch

        isRequestInProgress = true

        when (val result = service.requestData(lastRequestedPage)) {
            is DataResult.Success -> {
                cache.insert(result.data.shows)
                totalPages = result.data.totalPages
                lastRequestedPage++
                isRequestInProgress = false
            }
            is DataResult.Error -> {
                _errors.postValue(result)
                isRequestInProgress = false
            }
        }
    }
}