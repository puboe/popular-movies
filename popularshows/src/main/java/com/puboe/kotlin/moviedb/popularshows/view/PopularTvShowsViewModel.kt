package com.puboe.kotlin.moviedb.popularshows.view

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.puboe.kotlin.moviedb.core.entities.DataResult
import com.puboe.kotlin.moviedb.popularshows.data.LocalCache
import com.puboe.kotlin.moviedb.popularshows.data.ShowsDatabase
import com.puboe.kotlin.moviedb.popularshows.entities.ShowResult
import com.puboe.kotlin.moviedb.popularshows.entities.TvShow
import com.puboe.kotlin.moviedb.popularshows.entities.TvShowsRepository
import com.puboe.kotlin.moviedb.popularshows.extension.singleArgViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class PopularTvShowsViewModel @Inject constructor(
    private val repository: TvShowsRepository
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    private val _error = MutableLiveData<DataResult.Error?>()
    private val showResult = MutableLiveData<ShowResult>()

    val shows: LiveData<PagedList<TvShow>> = Transformations.switchMap(showResult) { it -> it.data }
    val errors: LiveData<DataResult.Error> = Transformations.switchMap(showResult) { it -> it.errors }

    val loading: LiveData<Boolean>
        get() = _loading

    // val error: LiveData<DataResult.Error?>
    //     get() = _error

    fun getPopularTvShows(context: Context) = viewModelScope.launch {
        showLoading()
        showResult.postValue(
            repository.getPopularTvShows(
                LocalCache(ShowsDatabase.getInstance(context).tvShowDao()),
                viewModelScope
            )
        )
        hideLoading()
    }

    // fun requestNextPage() = viewModelScope.launch {
    //     handleResult(repository.getNextPage())
    // }

    // fun listScrolled(visibleItemCount: Int, lastVisibleItemPosition: Int, totalItemCount: Int) {
    //     if (visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount) {
    //         requestNextPage()
    //     }
    // }

    // private fun handleResult(result: DataResult<List<TvShow>>) {
    //     when (result) {
    //         is DataResult.Success -> updateShows(result)
    //         is DataResult.Error -> showError(result)
    //     }
    // }

    // private fun updateShows(result: DataResult.Success<List<TvShow>>) {
    //     resetError()
    //     hideLoading()
    //     _shows.value = result.data
    // }

    private fun showError(error: DataResult.Error) {
        hideLoading()
        _error.value = error
    }

    private fun resetError() {
        _error.value = null
    }

    private fun showLoading() {
        _loading.value = true
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
