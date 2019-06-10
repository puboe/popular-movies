package com.puboe.kotlin.moviedb.popularshows.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.puboe.kotlin.moviedb.core.entities.DataResult
import com.puboe.kotlin.moviedb.popularshows.entities.PopularTvShows
import com.puboe.kotlin.moviedb.popularshows.entities.TvShow
import com.puboe.kotlin.moviedb.popularshows.entities.TvShowsRepository
import com.puboe.kotlin.moviedb.popularshows.extension.singleArgViewModelFactory
import com.puboe.kotlin.moviedb.popularshows.network.TvShowsParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PopularTvShowsViewModel @Inject constructor(
    private val repository: TvShowsRepository
) : ViewModel() {

    private var currentPage = 0
    private var totalPages = Integer.MAX_VALUE
    private var requestInProgress = false

    private val _shows = MutableLiveData<List<TvShow>>()
    private val _loading = MutableLiveData<Boolean>()
    private val _error = MutableLiveData<DataResult.Error?>()

    val shows: LiveData<List<TvShow>>
        get() = _shows

    val loading: LiveData<Boolean>
        get() = _loading

    val error: LiveData<DataResult.Error?>
        get() = _error

    fun getPopularTvShows() {
        requestPage(1)
    }

    fun requestNextPage() {
        if (currentPage + 1 <= totalPages) {
            requestPage(currentPage + 1)
        }
    }

    fun listScrolled(visibleItemCount: Int, lastVisibleItemPosition: Int, totalItemCount: Int) {
        if (visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount) {
            requestNextPage()
        }
    }

    private fun requestPage(page: Int) {
        if (requestInProgress) return

        resetError()
        showLoading()

        viewModelScope.launch {

            val result = repository.getPopularTvShows(TvShowsParams(page))
            when (result) {
                is DataResult.Success -> updateShows(result.data)
                is DataResult.Error -> showError(result)
            }
        }
    }

    private fun updateShows(result: PopularTvShows) =
        viewModelScope.launch {
            currentPage = result.page
            totalPages = result.totalPages
            _shows.value = withContext(Dispatchers.IO) {
                (_shows.value ?: emptyList()) + result.shows
            }
            hideLoading()
            requestInProgress = false
        }

    private fun showError(error: DataResult.Error) {
        _error.value = error
        requestInProgress = false
    }

    private fun resetError() {
        _error.value = null
    }

    private fun showLoading() {
        requestInProgress = true
        if (currentPage == 0) {
            _loading.value = true
        }
    }

    private fun hideLoading() {
        _loading.value = false
    }

    companion object {
        const val VISIBLE_THRESHOLD = 5

        /**
         * Factory for creating [PopularTvShowsViewModel]
         *
         * @param arg the repository to pass to [PopularTvShowsViewModel]
         */
        val FACTORY = singleArgViewModelFactory(::PopularTvShowsViewModel)
    }
}