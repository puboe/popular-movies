package com.puboe.kotlin.moviedb.popularshows.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.puboe.kotlin.moviedb.popularshows.entities.PopularTvShows
import com.puboe.kotlin.moviedb.popularshows.entities.TvShow
import com.puboe.kotlin.moviedb.popularshows.entities.TvShowsRepository
import com.puboe.kotlin.moviedb.popularshows.extension.singleArgViewModelFactory
import com.puboe.kotlin.moviedb.popularshows.network.TvShowsParams
import kotlinx.coroutines.launch
import javax.inject.Inject

class PopularTvShowsViewModel @Inject constructor(
    private val repository: TvShowsRepository
) : ViewModel() {

    private var popularTvShows: PopularTvShows? = null
    private val _shows = MutableLiveData<List<TvShow>>()
    private val _loading = MutableLiveData<Boolean>()
    private val _error = MutableLiveData<String?>()

    val shows: LiveData<List<TvShow>>
        get() = _shows

    val loading: LiveData<Boolean>
        get() = _loading

    val error: LiveData<String?>
        get() = _error

    fun getPopularTvShows() {
        _loading.value = true
        requestNextPage()
    }

    fun requestNextPage() {
        requestPage((popularTvShows?.page ?: 0) + 1)
    }

    private fun requestPage(page: Int) =
        _error.value = null
        viewModelScope.launch {
            popularTvShows = repository.getPopularTvShows(TvShowsParams(page))
            updateShows(popularTvShows?.shows)
        }

    private fun updateShows(results: List<TvShow>?) {
        _loading.value = false
        results?.let {
            _shows.value = (_shows.value ?: emptyList()) + it
        }
    }

    companion object {
        /**
         * Factory for creating [PopularTvShowsViewModel]
         *
         * @param arg the repository to pass to [PopularTvShowsViewModel]
         */
        val FACTORY = singleArgViewModelFactory(::PopularTvShowsViewModel)
    }
}