package com.puboe.kotlin.moviedb.core.entities

sealed class DataResult<out T : Any> {

    data class Success<out T : Any>(val data: T) : DataResult<T>()

    sealed class Error : DataResult<Nothing>() {
        object NetworkError : Error()
        object ServerError : Error()
        object ClientError : Error()
    }
}