package com.puboe.kotlin.moviedb.popularshows.data

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.puboe.kotlin.moviedb.popularshows.entities.TvShow

@Dao
interface TvShowDao {

    @Query("SELECT * FROM shows ORDER BY ranking")
    fun getTvShows(): DataSource.Factory<Int, TvShow>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(shows: List<TvShow>)
}
