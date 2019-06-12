package com.puboe.kotlin.moviedb.core.provider

interface DataProvider<in P, out T> {

    suspend fun requestData(params: P): T
}