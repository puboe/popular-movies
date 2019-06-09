package com.puboe.kotlin.moviedb.core.provider

interface DataMapper<in S, out T> {

    fun map(source: S): T
}
