package com.puboe.kotlin.moviedb.popularshows.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.puboe.kotlin.moviedb.popularshows.entities.TvShow

/**
 * The Room database for this app
 */
@Database(entities = [TvShow::class], version = 1, exportSchema = false)
abstract class ShowsDatabase : RoomDatabase() {
    abstract fun tvShowDao(): TvShowDao

    companion object {
        private const val DATABASE_NAME = "shows-database"

        @Volatile
        private var instance: ShowsDatabase? = null

        fun getInstance(context: Context): ShowsDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): ShowsDatabase {
            return Room.databaseBuilder(context, ShowsDatabase::class.java, DATABASE_NAME).build()
        }
    }
}