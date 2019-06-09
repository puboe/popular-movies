package com.puboe.kotlin.moviedb.popularshows.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.puboe.kotlin.moviedb.popularshows.BuildConfig
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
    private val _shows: MutableLiveData<List<TvShow>> = MutableLiveData()

    val shows: LiveData<List<TvShow>>
        get() = _shows

    fun getPopularTvShows() {
        requestNextPage()
    }

    fun requestNextPage() {
        requestPage((popularTvShows?.page ?: 0) + 1)
    }

    private fun requestPage(page: Int) =
        viewModelScope.launch {
            popularTvShows = repository.getPopularTvShows(TvShowsParams(page))
            updateShows(popularTvShows?.shows)
        }

    private fun updateShows(results: List<TvShow>?) {
        results?.let {
            val updatedList = _shows.value as? MutableList<TvShow> ?: ArrayList()
            updatedList.addAll(it)
            _shows.value = updatedList
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