package com.puboe.kotlin.moviedb.popularshows.data

import android.util.Log
import androidx.paging.DataSource
import com.puboe.kotlin.moviedb.popularshows.entities.TvShow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalCache(private val tvShowDao: TvShowDao) {

    /**
     * Insert a list of repos in the database, on a background thread.
     */
    suspend fun insert(shows: List<TvShow>) {
        Log.d(" LocalCache", "inserting ${shows.size} shows")
        withContext(Dispatchers.IO) {
            tvShowDao.insert(shows)
        }
    }

    /**
     * Request a LiveData<List<Repo>> from the Dao, based on a repo name. If the name contains
     * multiple words separated by spaces, then we're emulating the GitHub API behavior and allow
     * any characters between the words.
     * @param name repository name
     */
    fun getTvShows(): DataSource.Factory<Int, TvShow> {
        return tvShowDao.getTvShows()
    }
}