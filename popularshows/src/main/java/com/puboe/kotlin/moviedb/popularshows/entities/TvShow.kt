package com.puboe.kotlin.moviedb.popularshows.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shows")
data class TvShow(
    @PrimaryKey val id: Int,
    val name: String,
    val rating: Float,
    val overview: String,
    val poster: String?,
    val ranking: Int
)